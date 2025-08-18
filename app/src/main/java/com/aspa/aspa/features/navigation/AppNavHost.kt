package com.aspa.aspa.features.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.login.LoginViewModel
import com.aspa.aspa.features.login.google.googleSignInHandler
import com.aspa.aspa.features.login.google.rememberGoogleSignInClient
import com.aspa.aspa.features.login.navigation.LoginDestinations
import com.aspa.aspa.features.login.navigation.loginGraph
import com.aspa.aspa.features.main.MainScreen
import com.aspa.aspa.features.main.navigation.MainDestinations

@Composable
fun AppNavHost(isSignedIn: Boolean) {
    val navController = rememberNavController()

    val loginViewModel: LoginViewModel = viewModel()
    val googleSignInClient = rememberGoogleSignInClient()
    val googleSignInLauncher = googleSignInHandler(
        viewModel = loginViewModel,
        navController = navController
    )


    NavHost(
        navController = navController,
        startDestination = if (isSignedIn) MainDestinations.GRAPH else LoginDestinations.GRAPH
    ) {
        loginGraph(
            navController,
            onLoginSuccess = {
                navController.navigate(MainDestinations.GRAPH) {
                    popUpTo(LoginDestinations.GRAPH) { inclusive = true }
                    launchSingleTop = true
                }
            }
        )
//        mainGraph(navController) // 하단 탭이 있는 메인 그래프
        composable(MainDestinations.GRAPH) {
            MainScreen() // 여기서 bottom tabs + 내부 NavHost(mainGraph)
        }
    }
}