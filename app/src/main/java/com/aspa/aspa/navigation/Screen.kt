package com.aspa.aspa.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Nickname : Screen("nickname")
    object Home : Screen("home")
    object Mypage : Screen("mypage")
} 