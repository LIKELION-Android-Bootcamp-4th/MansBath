package com.aspa.aspa.ui.components.StudyNav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.aspa.aspa.features.study.StudyDetail.StudyDetailScreen
import com.aspa.aspa.features.study.StudyScreen
import com.aspa.aspa.features.study.StudyViewModel

object Graph {
    const val Study= "study_graph"
}

sealed class StudyScreenRoute(val route: String) {
    object Study : StudyScreenRoute("study?roadmapId={roadmapId}&questionId={questionId}")
    object StudyDetail : StudyScreenRoute("study_detail")

    fun study(roadmapId: String, questionId: String): String =
        "study?roadmapId=$roadmapId&questionId=$questionId"
}


fun NavGraphBuilder.studyGraph(navController: NavHostController) {
    navigation(
        startDestination = StudyScreenRoute.Study.route,
        route = Graph.Study
    ) {
        composable(
            route = StudyScreenRoute.Study.route,
            arguments = listOf(
                navArgument("roadmapId") { type = NavType.StringType },
                navArgument("questionId") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graph.Study)
            }
            val vm: StudyViewModel = hiltViewModel(parentEntry)
            val roadmapId = backStackEntry.arguments?.getString("roadmapId")
            val questionId = backStackEntry.arguments?.getString("questionId")

            StudyScreen(
                uiState = vm.uiState.collectAsStateWithLifecycle().value,
                onClickItem = { navController.navigate(StudyScreenRoute.StudyDetail.route) },
                onRefresh = { vm.fetchStudy() },
            )
        }
        composable(StudyScreenRoute.StudyDetail.route) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(Graph.Study)
            }
            val vm: StudyViewModel = hiltViewModel(parentEntry)

            StudyDetailScreen(
                uiState = vm.uiState.collectAsStateWithLifecycle().value,
                onRetry = { vm.fetchStudy() }
            )
        }
    }
}