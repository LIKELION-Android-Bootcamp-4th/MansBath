package com.aspa.aspa.features.login

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.dto.UserProfileDto
import com.aspa.aspa.data.local.datastore.DataStoreManager
import com.aspa.aspa.data.repository.AuthRepository
import com.aspa.aspa.data.repository.FcmRepository
import com.aspa.aspa.model.Provider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginState {
    object Idle : LoginState
    object Loading : LoginState
    data class Success(val user: UserProfileDto?) : LoginState
    data class Error(val message: String) : LoginState
}

sealed interface LogoutState {
    object Idle : LogoutState
    object Success : LogoutState
    data class Error(val message: String?) : LogoutState
}

sealed interface WithdrawState {
    object Idle : WithdrawState
    object Success : WithdrawState
    data class Error(val message: String?) : WithdrawState
}

sealed class PermissionState {
    object Idle : PermissionState() // 초기 상태
    object Granted : PermissionState() // 권한 허용
    data class Denied(val shouldShowRationale: Boolean) : PermissionState() // 권한 거부
}

@HiltViewModel //viewModel 등록 및 생성
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val credentialManager: CredentialManager,
    private val getCredentialRequest: GetCredentialRequest,
    private val fcmRepository: FcmRepository,
    private val auth: FirebaseAuth,
    private val dataStoreManager: DataStoreManager,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    companion object {
        private const val TAG = "LoginVM"
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

    private val _logoutState = MutableStateFlow<LogoutState>(LogoutState.Idle)
    val logoutState = _logoutState.asStateFlow()

    private val _withdrawState = MutableStateFlow<WithdrawState>(WithdrawState.Idle)
    val withdrawState = _withdrawState.asStateFlow()

    private val _nicknameState = MutableStateFlow<String>("조회 중..")
    val nicknameState: StateFlow<String> = _nicknameState

    private val _providerState = MutableStateFlow<Provider?>(null)
    val providerState: StateFlow<Provider?> = _providerState

    private val _permissionState = MutableStateFlow<PermissionState>(PermissionState.Idle)
    val permissionState = _permissionState.asStateFlow()

    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val isGranted = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (isGranted) {
                _permissionState.update { PermissionState.Granted }
            } else {
                // 아직 권한이 없다면 초기 상태(Idle)를 유지
                _permissionState.update { PermissionState.Idle }
            }
        }
    }

    fun signInWithGoogleCredential(activity: Activity, onSuccess: () -> Unit) {

        viewModelScope.launch {
            _loginState.value = LoginState.Loading

            try {
                // 1) CredentialManager에서 자격 증명 받기
                val response = credentialManager.getCredential(activity, getCredentialRequest)
                val cred = response.credential

                // 2) Google ID 토큰 추출
                val idToken = when (cred) {
                    is CustomCredential -> {

                        if (cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {

                            val token = GoogleIdTokenCredential.createFrom(cred.data).idToken
                            token
                        } else {
                            null
                        }
                    }

                    else -> {
                        Log.w(TAG, "문제가 발생 하였습니다 ")
                        null
                    }
                }

                if (idToken.isNullOrBlank()) {
                    Log.e(TAG, "토큰을 가져오지 못함.")
                    return@launch
                }

                // 3) Repository 로그인 + 프로필 저장
                var repoResult: Result<UserProfileDto> = authRepository.signInWithGoogle(idToken)

                repoResult
                    .onSuccess { dto ->
                        Log.d(TAG, "성공하였습니다.")
                        _loginState.value = LoginState.Success(dto)
                        updateFcmToken()
                        getProvider()
                        dataStoreManager.setLastLoginProvider(Provider.GOOGLE)

                        onSuccess()
                    }
                    .onFailure { e ->
                        Log.e(TAG, "repo failure: ", e)
                    }

            } catch (error: Exception) {
                Log.e(TAG, "문제가 발생했습니다. ${error.message}")

            }
        }
    }

    fun signInWithNaver(accessToken: String?) = viewModelScope.launch {
        _loginState.value = LoginState.Loading
        authRepository.signInWithNaver(accessToken)
            .onSuccess {
                _loginState.value = LoginState.Success(null)
                updateFcmToken()
                getProvider()
                dataStoreManager.setLastLoginProvider(Provider.NAVER)
            }
            .onFailure { e ->
                _loginState.value = LoginState.Error(e.message ?: "❌ 네이버 로그인 실패")
            }
    }

    fun signInWithKakao(context: Context) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            handleKakaoLogin(context)
        }
    }

    private fun handleKakaoLogin(context: Context) {
        // 카카오 계정 로그인 공통 callback
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("KakaoLogin", "카카오 계정 로그인 실패", error)
                _loginState.value = LoginState.Error(error.message ?: "❌ 카카오 계정 로그인 실패")

            } else if (token != null) {
                Log.i("KakaoLogin", "카카오 계정 로그인 성공: ${token.accessToken}")

                // 카카오 ID 토큰을 사용하여 Firebase에 인증
                firebaseAuthWithKakao(token.accessToken, context as Activity)
            }
        }
        // 카카오 로그인 함수: 카카오톡이 설치되어 있으면 카카오톡, 아니면 계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("KakaoLogin", "카카오톡으로 로그인 실패", error)

                    // 디바이스 권한 요청 화면에서 로그인 취소시 카카오계정 로그인 시도 X
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        _loginState.value =
                            LoginState.Error(error.message ?: "❌ 카카오 계정 로그인을 취소하셨습니다.")
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 계정이 없는 경우 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i("KakaoLogin", "카카오톡으로 로그인 성공: ${token.accessToken}")

                    // 카카오 ID 토큰을 사용하여 Firebase에 인증
                    firebaseAuthWithKakao(token.accessToken, context as Activity)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun firebaseAuthWithKakao(kakaoAccessToken: String, context: Activity) {
        val auth = Firebase.auth

        // Firebase Console에서 OpenID Connect 제공업체 ID로 설정했던 값
        val provider = OAuthProvider.newBuilder("oidc.kakao")
            .addCustomParameter("access_token", kakaoAccessToken)
            .setScopes(listOf("openid", "profile_nickname", "account_email")) // -필요한 스코프를 추가
            .build()

        auth.startActivityForSignInWithProvider(context, provider)
            .addOnSuccessListener { authResult ->
                // Firebase 인증 성공
                Log.d("FirebaseAuth", "Firebase 로그인 성공")

                val user = authResult.user
                val db = Firebase.firestore
                val userUid = user?.uid ?: ""
                Log.d("FirebaseAuth", userUid)

                val userDoc = db.collection("users").document(userUid)
                userDoc.get()
                    .addOnSuccessListener { document ->
                        if (document.exists()) {
                            userDoc.update("lastLogin", FieldValue.serverTimestamp())
                                .addOnSuccessListener {
                                    Log.d("Firestore", "최근 로그인 정보 갱신 성공")
                                    updateFcmToken()
                                    viewModelScope.launch {
                                        dataStoreManager.setLastLoginProvider(Provider.KAKAO)
                                    }
                                    getProvider()
                                    _loginState.value = LoginState.Success(null)
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", "최근 로그인 정보 갱신 실패", e)
                                }
                        } else {
                            // 추가 정보 확인
                            val additionalUserInfo = authResult.additionalUserInfo
                            if (additionalUserInfo != null) {
                                val profile = additionalUserInfo.profile
                                val userProfile = hashMapOf(
                                    "uid" to userUid,
                                    "email" to profile?.get("email") as? String,
                                    "name" to profile?.get("nickname") as? String,
                                    "provider" to "kakao",
                                    "lastLogin" to FieldValue.serverTimestamp()
                                )
                                if (user != null) {
                                    userDoc
                                        .set(userProfile)
                                        .addOnSuccessListener {
                                            Log.d("Firestore", "Firestore에 사용자 프로필 저장 성공!")
                                            updateFcmToken()
                                            viewModelScope.launch {
                                                dataStoreManager.setLastLoginProvider(Provider.KAKAO) // ✅ suspend 안전하게 실행
                                            }
                                            getProvider()
                                            _loginState.value = LoginState.Success(null)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("Firestore", "Firestore에 프로필 저장 실패: ", e)
                                            _loginState.value =
                                                LoginState.Error(e.message ?: "❌ DB에 프로필 저장 실패")
                                        }
                                }
                            } else {
                                Log.e("Firestore", "프로필 추가 정보가 없습니다.")
                                _loginState.value =
                                    LoginState.Error("프로필 정보를 받아오는 데에 실패했습니다. 다시 시도해주세요.")
                            }
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.w("Firestore", "문서 읽기 실패", e)
                        _loginState.value = LoginState.Error(e.message ?: "❌ DB에 프로필 저장 실패")
                    }
            }
            .addOnFailureListener { e ->
                // Firebase 인증 실패
                Log.e("FirebaseAuth", "Firebase 로그인 실패", e)
                _loginState.value = LoginState.Error(e.message ?: "❌ 카카오 로그인 실패")
            }
    }

    // =================================================================================

    fun signOut(context: Context) {
        viewModelScope.launch {
            // fcm 토큰 삭제
            deleteFcmToken()
            // 소셜 로그아웃
            authRepository.fetchProvider()
                .onSuccess { provider ->
                    when (provider) {
                        Provider.GOOGLE -> signOutGoogle(context)
                        Provider.KAKAO -> signOutKakao()
                        Provider.NAVER -> signOutNaver()
                    }
                    // Firebase 세션 로그아웃
                    auth.signOut()
                    Log.i("LOGOUT", "✅ 파이어베이스 로그아웃 완료")

                    _logoutState.value = LogoutState.Success
                }
                .onFailure { e ->
                    _logoutState.value = LogoutState.Error(e.message)
                }
        }
    }


    private fun signOutGoogle(
        context: Context
    ) {
        /*
         * 현재 앱에서 채택한 구글 로그인 방식으로는 앱 내부에 idToken을 저장하지 않지만
         * 안드로이드 OS에서 OS가 가지고 있는 구글 계정 정보를 통해 자동적으로 최근 로그인했던 구글 계정 로그인이 가능하므로
         * 명시적으로 gso를 통해 토큰을 삭제(signout)합니다.
         */
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        googleSignInClient.signOut()
            .addOnCompleteListener {
                Log.i("LOGOUT", "✅ 구글 로그아웃 완료")
            }
            .addOnFailureListener { e ->
                Log.d("LOGOUT", "❌ 구글 로그아웃 실패")
                throw e
            }
    }

    private fun signOutKakao() {
        UserApiClient.instance.logout { e ->
            if (e == null) {
                Log.i("LOGOUT", "✅ 카카오 로그아웃 성공")
            } else {
                Log.e("LOGOUT", "❌ 카카오 로그아웃 실패", e)
                throw e
            }
        }
    }

    private fun signOutNaver() {
        NaverIdLoginSDK.logout()
        Log.i("LOGOUT", "✅ 네이버 로그아웃 성공")
    }

    fun resetLogoutState() {
        _logoutState.value = LogoutState.Idle
    }

    fun withdraw(context: Context) {
        viewModelScope.launch {
            // 소셜 unlink
            authRepository.fetchProvider()
                .onSuccess { provider ->
                    when (provider) {
                        Provider.GOOGLE -> withdrawGoogle(context)
                        Provider.KAKAO -> withdrawKakao()
                        Provider.NAVER -> withdrawNaver()
                    }
                    // Firebase 계정 회원탈퇴  // 자동으로 onDelete 트리거를 통한 functions 호출
                    auth.currentUser?.delete()
                        ?.addOnSuccessListener {
                            Log.i("WITHDRAW", "✅ 파이어베이스 계정 삭제 완료")
                            _withdrawState.value = WithdrawState.Success

                            viewModelScope.launch {
                                dataStoreManager.setLastLoginProvider(null)
                            }

                        }
                        ?.addOnFailureListener { e ->
                            Log.i("WITHDRAW", "❌ 파이어베이스 계정 삭제 실패")
                            _withdrawState.value = WithdrawState.Error(e.message)
                        }
                }
                .onFailure { e ->
                    _withdrawState.value = WithdrawState.Error(e.message)
                }
        }
    }

    private fun withdrawGoogle(context: Context) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build()
        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        googleSignInClient.revokeAccess()
            .addOnCompleteListener {
                Log.i("WITHDRAW", "✅ 구글 계정 연동 해제 완료")
            }
            .addOnFailureListener { e ->
                Log.d("WITHDRAW", "❌ 구글 계정 연동 해제 실패")
                throw e
            }
    }

    private fun withdrawKakao() {
        UserApiClient.instance.unlink { e ->
            if (e == null) {
                Log.i("WITHDRAW", "✅ 카카오 계정 연동 해제 성공")
            } else {
                Log.e("WITHDRAW", "❌ 카카오 계정 연동 해제 실패", e)
                throw e
            }
        }
    }

    private fun withdrawNaver() {
        NidOAuthLogin().callDeleteTokenApi(object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }

            override fun onFailure(httpStatus: Int, message: String) {
                Log.e("WITHDRAW", "❌ 네이버 계정 연동 해제 실패: $httpStatus $message")
            }

            override fun onSuccess() {
                Log.d("WITHDRAW", "✅ 네이버 계정 연동 해제 성공")
            }
        })
        Log.i("WITHDRAW", "✅ 네이버 계정 연동 해제 성공")
    }

    fun resetWithdrawState() {
        _withdrawState.value = WithdrawState.Idle
    }

    // =================================================================================

    fun getProvider() {
        viewModelScope.launch {
            authRepository.fetchProvider()
                .onSuccess { provider -> _providerState.value = provider }
                .onFailure {e ->
                    _providerState.value = null
                    Log.e("PROVIDER", "${e.message}")
                }
        }
    }

    fun getNickname() {
        viewModelScope.launch {
            authRepository.fetchNickname()
                .onSuccess { nickname ->
                    _nicknameState.value = nickname
                }
        }
    }

    fun updateFcmToken() {
        viewModelScope.launch {
            val token = fcmRepository.getToken()
            if (token != null) {
                fcmRepository.updateFcmToken(auth.uid!!, token)
            }
        }
    }
    
    fun deleteFcmToken() {
        viewModelScope.launch {
            fcmRepository.deleteFcmToken(auth.uid!!)
        }
    }
    
    fun onPermissionResult(isGranted: Boolean, shouldShowRationale: Boolean) {
        if (isGranted) {
            _permissionState.update { PermissionState.Granted }
        } else {
            _permissionState.update { PermissionState.Denied(shouldShowRationale) }
        }
    }
}
