package com.aspa.aspa.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aspa.aspa.features.home.HomeScreen
import com.aspa.aspa.features.login.LoginScreen
import com.aspa.aspa.features.mypage.MypageScreen
import com.aspa.aspa.features.nickname.NicknameScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Login.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = { user ->
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onNeedNickname = { user ->
                    navController.navigate(Screen.Nickname.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Nickname.route) {
            NicknameScreen(
                onPrevious = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Nickname.route) { inclusive = true }
                    }
                },
                onStart = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Nickname.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToMypage = {
                    navController.navigate(Screen.Mypage.route)
                }
            )
        }
        
        composable(Screen.Mypage.route) {
            MypageScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Mypage.route) { inclusive = true }
                    }
                },
                onNavigateToQuiz = {
                    // TODO: Quiz 화면으로 이동
                },
                onNavigateToRoadmap = {
                    // TODO: Roadmap 화면으로 이동
                },
                onNavigateToMypage = {
                    // 현재 화면이므로 아무것도 하지 않음
                }
            )
        }
    }
} 