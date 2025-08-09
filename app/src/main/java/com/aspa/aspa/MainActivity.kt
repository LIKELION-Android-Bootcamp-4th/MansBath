package com.aspa.aspa

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aspa.aspa.features.home.HomeScreen
import com.aspa.aspa.ui.theme.AspaTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.OAuthProvider
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AspaTheme {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = {
                            handleKakaoLogin()
                        }
                    ) { Text("Kakao Login!") }
                }

            }
        }
    }
}

private fun handleKakaoLogin() {
    // 카카오 로그인 함수
    UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
        if (error != null) {
            Log.e("KakaoLogin", "카카오 로그인 실패", error)
        } else if (token != null) {
            Log.i("KakaoLogin", "카카오 로그인 성공: ${token.accessToken}")

            // 카카오 ID 토큰을 사용하여 Firebase에 인증
            firebaseAuthWithKakao(token.accessToken)
        }
    }
}

private fun firebaseAuthWithKakao(kakaoAccessToken: String) {
    val auth = Firebase.auth

    // Firebase Console에서 OpenID Connect 제공업체 ID로 설정했던 값
    val provider = OAuthProvider.newBuilder("oidc.kakao")
        .addCustomParameter("access_token", kakaoAccessToken)
        .setScopes(listOf("openid", "profile", "email")) // 필요한 스코프를 추가
        .build()

    auth.startActivityForSignInWithProvider(this, provider)
        .addOnSuccessListener { authResult ->
            // Firebase 인증 성공
            Log.d("FirebaseAuth", "Firebase 로그인 성공: ${authResult.user?.uid}")
            // TODO: 로그인 성공 후 다음 화면으로 이동 등의 로직 구현
        }
        .addOnFailureListener { e ->
            // Firebase 인증 실패
            Log.e("FirebaseAuth", "Firebase 로그인 실패", e)
        }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AspaTheme {
        Greeting("Android")
    }
}