package com.aspa.aspa.features.roadmap.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aspa.aspa.features.roadmap.RoadmapDetailScreen
import com.aspa.aspa.features.roadmap.RoadmapListScreen
import com.aspa.aspa.features.roadmap.components.RoadmapDialog

object RoadmapDestinations {
    const val ROADMAP_LIST = "roadmap?questionId={questionId}"
    const val ROADMAP_DETAIL = "roadmap/detail/{id}"
    const val ROADMAP_DIALOG = "roadmap/dialog/{id}"
    const val STUDY = "study"
    const val QUIZ = "quiz"
}

fun NavGraphBuilder.roadmapGraph(navController: NavController) {
    composable(
        route = RoadmapDestinations.ROADMAP_LIST,
        arguments = listOf(navArgument("questionId") {
            type = NavType.StringType
            defaultValue = ""
        })
    ) {backStackEntry ->
        val questionId = backStackEntry.arguments?.getString("questionId") ?: ""
        RoadmapListScreen(
            navController = navController,
            questionId = questionId
        )
    }

    composable(
        route = RoadmapDestinations.ROADMAP_DETAIL,
        arguments = listOf(navArgument("id") { type = NavType.StringType })
    ) { backStackEntry ->
        val roadmapId = backStackEntry.arguments?.getString("id") ?: ""
        RoadmapDetailScreen(roadmapId, navController)
    }

    composable(
        route = RoadmapDestinations.ROADMAP_DIALOG,
        arguments = listOf(navArgument("id") { type = NavType.StringType })
    ) { backStackEntry ->
        val sectionId = backStackEntry.arguments?.getString("id") ?: ""
        RoadmapDialog(
            sectionId = sectionId,
            navController = navController
        )
    }

    composable(RoadmapDestinations.STUDY) {
//        StudyScreen()  // todo: 스터디 연동
    }
}