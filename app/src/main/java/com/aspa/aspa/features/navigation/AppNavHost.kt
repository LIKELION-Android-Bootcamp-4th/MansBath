package com.aspa.aspa.features.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.SplashDestinations
import com.aspa.aspa.SplashScreen
import com.aspa.aspa.features.login.navigation.loginGraph
import com.aspa.aspa.features.main.MainScreen
import com.aspa.aspa.features.main.navigation.MainDestinations

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = SplashDestinations.SPLASH) {

        loginGraph(navController)

        composable(MainDestinations.MAIN) {
            MainScreen(navController)
        }

        composable(SplashDestinations.SPLASH) {
            SplashScreen(navController)
        }
    }
}