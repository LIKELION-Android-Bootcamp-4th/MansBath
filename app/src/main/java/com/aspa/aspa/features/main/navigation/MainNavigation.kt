package com.aspa.aspa.features.main.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.home.navigation.homeGraph
import com.aspa.aspa.features.mypage.navigation.mypageGraph
import com.aspa.aspa.features.quiz.navigation.quizGraph
import com.aspa.aspa.features.roadmap.navigation.roadmapGraph

object MainDestinations { const val GRAPH = "main_graph" }

enum class BottomTab(val route: String) {
    Home("home"), Roadmap("roadmap"), Quiz("quiz"), MyPage("mypage")
}

fun NavGraphBuilder.mainGraph(navController: NavController) {
    navigation(
        startDestination = BottomTab.Home.route,
        route = MainDestinations.GRAPH
    ) {
        homeGraph(navController = navController, homeViewModel = HomeViewModel())
        roadmapGraph(navController = navController)
        quizGraph(navController = navController)
        mypageGraph(navController = navController)
    }
}