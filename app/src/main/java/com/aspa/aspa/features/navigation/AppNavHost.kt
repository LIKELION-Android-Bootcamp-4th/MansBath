package com.aspa.aspa.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aspa.aspa.OnboardingDestinations
import com.aspa.aspa.OnboardingScreen
import com.aspa.aspa.SplashDestinations
import com.aspa.aspa.SplashScreen
import com.aspa.aspa.features.login.navigation.loginGraph
import com.aspa.aspa.features.main.MainScreen
import com.aspa.aspa.features.main.navigation.MainDestinations

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = SplashDestinations.SPLASH) {

        loginGraph(
            navController = navController,
        )

        composable(
            route = "${MainDestinations.MAIN}?redirect={redirect}",
            arguments = listOf(
                navArgument("redirect") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = false
                }
            )
        ) { backStackEntry ->
            val redirect = backStackEntry.arguments?.getString("redirect")
            MainScreen(rootNavController = navController, redirect = redirect)
        }

        composable(SplashDestinations.SPLASH) {
            SplashScreen(
                navController = navController,
            )
        }

        composable(OnboardingDestinations.ONBOARDING) {
            OnboardingScreen(
                navController = navController,
            )
        }
    }
}