package com.aspa.aspa.ui.components.StudyNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.study.StudyDetail.StudyDetailScreen
import com.aspa.aspa.features.study.StudyScreen
import com.aspa.aspa.features.study.StudyViewModel

object Graph {
    const val Study= "study_graph"
}

sealed class StudyScreenRoute(val route: String) {
    object Study : StudyScreenRoute("study")
    object StudyDetail : StudyScreenRoute("study_detail")
}

@Composable
fun StudyNav(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = StudyScreenRoute.Study.route, route = Graph.Study ){
        composable(StudyScreenRoute.Study.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graph.Study)
            }
            val vm: StudyViewModel = hiltViewModel(parentEntry)

           StudyScreen(
               uiState = vm.uiState.collectAsStateWithLifecycle().value,
               onClickItem = { navController.navigate(StudyScreenRoute.StudyDetail.route)},
               onRefresh = { vm.fetchStudy() }
           )
        }
        composable(StudyScreenRoute.StudyDetail.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry){
                navController.getBackStackEntry(Graph.Study)
            }
            val vm : StudyViewModel = hiltViewModel(parentEntry)

            StudyDetailScreen(
                uiState = vm.uiState.collectAsStateWithLifecycle().value,
                onRetry = {vm.fetchStudy()}
            )
        }
    }
}