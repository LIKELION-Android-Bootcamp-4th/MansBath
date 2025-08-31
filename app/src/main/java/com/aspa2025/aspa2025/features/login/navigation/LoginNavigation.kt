package com.aspa2025.aspa2025.features.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.aspa2025.aspa2025.features.login.LoginScreen

object LoginDestinations {
    const val LOGIN_GRAPH_ROUTE = "login_graph"
    const val LOGIN = "login"
}

fun NavGraphBuilder.loginGraph(
    navController: NavController,
) {
    navigation(
        startDestination = LoginDestinations.LOGIN,
        route = LoginDestinations.LOGIN_GRAPH_ROUTE
    ) {
        composable(LoginDestinations.LOGIN) {
            LoginScreen(
                navController,
            )
        }
    }
}