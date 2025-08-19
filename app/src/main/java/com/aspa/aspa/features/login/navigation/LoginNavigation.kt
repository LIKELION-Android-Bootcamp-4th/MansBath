package com.aspa.aspa.features.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.aspa.aspa.features.login.LoginScreen
import com.aspa.aspa.features.login.NicknameScreen
import com.aspa.aspa.features.main.MainScreen

object LoginDestinations {
    const val GRAPH = "login_graph"
    const val LOGIN = "login"
    const val NICKNAME = "nickname"
}

fun NavGraphBuilder.loginGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit
) {
    navigation(
        startDestination = LoginDestinations.LOGIN,
        route = LoginDestinations.GRAPH
    ) {
        composable(LoginDestinations.LOGIN) {
            LoginScreen(
                navController as NavHostController,
//                onGoogleSignInClick = {
//                    googleSignInLauncher.launch(googleSignInClient.signInIntent)
//                },
//                onKakaoSignInClick = { /* TODO: Kakao 로그인  */ },
//                onNaverSignInClick = { /* TODO: Naver 로그인  */ },
//                onLoginClick = {
//                    Auth.uid = "test-user-for-web"
//                    navController.navigate("nickname")
//                }
            )
        }

        composable(LoginDestinations.NICKNAME) {
            NicknameScreen(
                onNavigateToPrevious = {
                    navController.popBackStack()
                },
                onNavigateToNext = { _finalNickname ->  // todo: 닉네임 누락 // viewModel에서 처리
                    // 닉네임 확정 → 상위(AppNavHost)로 성공 신호 전달
                    onLoginSuccess()
                }
            )
        }

//        composable("nickname") {
//            NicknameScreen(
//                onNavigateToPrevious = {
//                    navController.popBackStack()
//                },
//                onNavigateToNext = { finalNickname ->
//                    navController.navigate("main/$finalNickname") {
//                        popUpTo("login") { inclusive = true }
//                    }
//                }
//            )
//        }

        composable("main") { backStackEntry ->
            MainScreen()
        }
    }
}