package com.aspa.aspa

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.login.LoginScreen
import com.aspa.aspa.features.login.NicknameScreen
import com.aspa.aspa.features.main.MainScreen
import com.aspa.aspa.ui.theme.AspaTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AspaTheme {
                AppNavigation()
            }
        }
    }
}

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

private fun handleKakaoLogin(context: Context) {
    // 카카오 계정 로그인 공통 callback
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e("KakaoLogin", "카카오 계정 로그인 실패", error)
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
            val userUid = "kakao:${user?.uid}"
            Log.d("FirebaseAuth", userUid)

            val userDoc = db.collection("users").document(userUid)
            userDoc.get()
                .addOnSuccessListener { document ->
                    if(document.exists()) {
                        userDoc.update("lastLogin",FieldValue.serverTimestamp())
                            .addOnSuccessListener {
                                Log.d("Firestore", "최근 로그인 정보 갱신 성공")
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
                                    }
                                    .addOnFailureListener { e ->
                                        Log.e("Firestore", "Firestore에 프로필 저장 실패: ", e)
                                    }
                            }
                        }
                        else {
                            Log.e("Firestore", "프로필 추가 정보가 없습니다.")
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "문서 읽기 실패", e)
                }
        }
        .addOnFailureListener { e ->
            // Firebase 인증 실패
            Log.e("FirebaseAuth", "Firebase 로그인 실패", e)
        }
}

//--------------------------------------------------------------------------------------------------
//--------------------------------------------------------------------------------------------------

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(navController)
        }
        composable("nickname") {
            NicknameScreen(
                onNavigateToPrevious = {
                    navController.popBackStack()
                },
                onNavigateToNext = {
                    // "main"으로 이동하도록 수정
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("main") {
            // navController를 전달하지 않음
            MainScreen()
        }

    }
}