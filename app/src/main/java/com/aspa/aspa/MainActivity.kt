package com.aspa.aspa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.aspa.aspa.features.login.LoginScreen
import com.aspa.aspa.features.nickname.NicknameScreen
import com.aspa.aspa.features.mypage.MypageScreen
import com.aspa.aspa.ui.theme.AspaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AspaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    var currentScreen by remember { mutableStateOf("login") }
    var nickname by remember { mutableStateOf("") }
    
    when (currentScreen) {
        "login" -> {
            LoginScreen(
                onGoogleSignInClick = { /* TODO: Google 로그인 */ },
                onKakaoSignInClick = { /* TODO: Kakao 로그인 */ },
                onNaverSignInClick = { /* TODO: Naver 로그인 */ },
                onLoginClick = {
                    currentScreen = "nickname"
                }
            )
        }
        "nickname" -> {
            NicknameScreen(
                nickname = nickname,
                onNicknameChange = { nickname = it },
                onPrevious = {
                    currentScreen = "login"
                },
                onStart = {
                    currentScreen = "mypage"
                }
            )
        }
        "mypage" -> {
            MypageScreen(
                nickname = nickname,
                onLogout = {
                    currentScreen = "login"
                },
                onNavigateToHome = { /* TODO: 홈으로 이동 */ },
                onNavigateToQuiz = { /* TODO: 퀴즈로 이동 */ },
                onNavigateToRoadmap = { /* TODO: 로드맵으로 이동 */ },
                onNavigateToMypage = { /* TODO: 마이페이지로 이동 */ },
                onNicknameChange = { newNickname ->
                    nickname = newNickname
                }
            )
        }
    }
}