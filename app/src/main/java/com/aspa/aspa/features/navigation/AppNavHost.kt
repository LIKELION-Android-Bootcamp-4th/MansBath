package com.aspa.aspa.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.login.navigation.LoginDestinations
import com.aspa.aspa.features.login.navigation.loginGraph
import com.aspa.aspa.features.main.MainScreen
import com.aspa.aspa.features.main.navigation.MainDestinations

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = LoginDestinations.LOGIN_GRAPH_ROUTE) {

        loginGraph(navController)

        composable(MainDestinations.MAIN) {
            // navController를 전달하지 않음
            MainScreen(navController)
        }
    }
}