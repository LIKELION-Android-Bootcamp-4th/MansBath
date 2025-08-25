package com.aspa.aspa.features.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.aspa.aspa.data.local.datastore.DataStoreManager
import com.aspa.aspa.features.login.LoginScreen

object LoginDestinations {
    const val LOGIN_GRAPH_ROUTE = "login_graph"
    const val LOGIN = "login"
}

fun NavGraphBuilder.loginGraph(
    navController: NavController,
    dataStoreManager: DataStoreManager
) {
    navigation(
        startDestination = LoginDestinations.LOGIN,
        route = LoginDestinations.LOGIN_GRAPH_ROUTE
    ) {
        composable(LoginDestinations.LOGIN) {
            LoginScreen(
                navController,
                dataStoreManager
            )
        }
    }
}