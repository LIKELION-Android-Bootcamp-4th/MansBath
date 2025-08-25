package com.aspa.aspa.features.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.OnboardingDestinations
import com.aspa.aspa.OnboardingScreen
import com.aspa.aspa.SplashDestinations
import com.aspa.aspa.SplashScreen
import com.aspa.aspa.data.local.datastore.DataStoreManager
import com.aspa.aspa.features.login.navigation.loginGraph
import com.aspa.aspa.features.main.MainScreen
import com.aspa.aspa.features.main.navigation.MainDestinations

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }

    NavHost(navController = navController, startDestination = SplashDestinations.SPLASH) {

        loginGraph(
            navController = navController,
            dataStoreManager = dataStoreManager
        )

        composable(MainDestinations.MAIN) {
            MainScreen(navController)
        }

        composable(SplashDestinations.SPLASH) {
            SplashScreen(
                navController = navController,
                dataStoreManager = dataStoreManager
            )
        }

        composable(OnboardingDestinations.ONBOARDING) {
            OnboardingScreen(
                navController = navController,
                dataStoreManager = dataStoreManager
            )
        }
    }
}