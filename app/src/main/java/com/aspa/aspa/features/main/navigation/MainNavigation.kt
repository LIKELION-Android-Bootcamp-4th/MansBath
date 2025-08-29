package com.aspa.aspa.features.main.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.home.navigation.HomeDestinations
import com.aspa.aspa.features.home.navigation.homeGraph
import com.aspa.aspa.features.mypage.navigation.mypageGraph
import com.aspa.aspa.features.quiz.navigation.quizGraph
import com.aspa.aspa.features.roadmap.navigation.roadmapGraph
import com.aspa.aspa.ui.components.MistakeNav.mistakeGraph
import com.aspa.aspa.ui.components.StudyNav.studyGraph

object MainDestinations { const val MAIN = "main" }


@Composable
fun MainNavigation(
    rootNavController: NavHostController,
    innerNavController: NavHostController,
    innerPadding: PaddingValues,
    homeViewModel: HomeViewModel
) {

    NavHost(
        navController = innerNavController,
        startDestination = HomeDestinations.HOME_GRAPH_ROUTE,
        modifier = Modifier.padding(innerPadding)
    ) {
        homeGraph(navController = innerNavController, homeViewModel = homeViewModel)
        roadmapGraph(navController = innerNavController)
        quizGraph(navController = innerNavController)
        mistakeGraph(navController = innerNavController)
        mypageGraph(rootNavController = rootNavController,innerNavController = innerNavController)
        studyGraph(navController = innerNavController)
    }
}
