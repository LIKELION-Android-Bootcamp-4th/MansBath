package com.aspa.aspa.features.login

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.dto.UserProfileDto
import com.aspa.aspa.data.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface LoginState {
    object Idle : LoginState
    object Loading : LoginState
    data class Success(val user: UserProfileDto?) : LoginState
    data class Error(val message: String) : LoginState
}

@HiltViewModel //viewModel 등록 및 생성
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val credentialManager: CredentialManager,
    private val getCredentialRequest: GetCredentialRequest
) : ViewModel() {

    companion object { private const val TAG = "LoginVM" }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState = _loginState.asStateFlow()

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
                        onSuccess()
                    }
                    .onFailure { e ->
                        Log.e(TAG, "repo failure: ", e)
                    }

            }catch (error : Exception){
                Log.e(TAG,"문제가 발생했습니다. ${error.message}")

            }
        }
    }

    fun signInWithNaver(accessToken: String?) = viewModelScope.launch {
        _loginState.value = LoginState.Loading
        authRepository.signInWithNaver(accessToken)
            .onSuccess { _loginState.value = LoginState.Success(null) }
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
        if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e("KakaoLogin", "카카오톡으로 로그인 실패", error)

                    // 디바이스 권한 요청 화면에서 로그인 취소시 카카오계정 로그인 시도 X
                    if(error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        _loginState.value = LoginState.Error(error.message ?: "❌ 카카오 계정 로그인을 취소하셨습니다.")
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
                        if(document.exists()) {
                            userDoc.update("lastLogin",FieldValue.serverTimestamp())
                                .addOnSuccessListener {
                                    Log.d("Firestore", "최근 로그인 정보 갱신 성공")
                                    _loginState.value = LoginState.Success(null)
                                }
                                .addOnFailureListener { e ->
                                    Log.e("Firestore", "최근 로그인 정보 갱신 실패", e)
                                }
                        }
                        else {
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
                                            _loginState.value = LoginState.Success(null)
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e("Firestore", "Firestore에 프로필 저장 실패: ", e)
                                            _loginState.value = LoginState.Error(e.message ?: "❌ DB에 프로필 저장 실패")
                                        }
                                }
                            }
                            else {
                                Log.e("Firestore", "프로필 추가 정보가 없습니다.")
                                _loginState.value = LoginState.Error("프로필 정보를 받아오는 데에 실패했습니다. 다시 시도해주세요.")
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
}
