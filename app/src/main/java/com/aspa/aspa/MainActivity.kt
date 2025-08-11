package com.aspa.aspa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.login.LoginScreen
import com.aspa.aspa.features.login.LoginViewModel
import com.aspa.aspa.features.login.NicknameScreen
import com.aspa.aspa.features.login.google.googleSignInHandler
import com.aspa.aspa.features.login.google.rememberGoogleSignInClient
import com.aspa.aspa.features.main.MainScreen
import com.aspa.aspa.model.Auth
import com.aspa.aspa.ui.theme.AspaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AspaTheme { }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()


    val googleSignInClient = rememberGoogleSignInClient()

    val googleSignInLauncher = googleSignInHandler(
        viewModel = loginViewModel,
        navController = navController
    )

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(
                onGoogleSignInClick = {
                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
                },
                onKakaoSignInClick = { /* TODO: Kakao 로그인  */ },
                onNaverSignInClick = { /* TODO: Naver 로그인  */ },
                onLoginClick = {
                    Auth.uid = "test-user-for-web"
                    navController.navigate("nickname")
                }
            )
        }

        composable("nickname") {
            NicknameScreen(
                onNavigateToPrevious = {
                    navController.popBackStack()
                },
                onNavigateToNext = { finalNickname ->
                    navController.navigate("main/$finalNickname") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("main/{nickname}") { backStackEntry ->
            val nickname = backStackEntry.arguments?.getString("nickname") ?: "사용자"
            MainScreen(
                nickname = nickname,
                onLogout = {
                    Auth.uid = null
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                }
            )
        }
    }
}