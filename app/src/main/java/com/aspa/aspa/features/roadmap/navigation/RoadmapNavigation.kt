package com.aspa.aspa.features.roadmap.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.roadmap.RoadmapDetailScreen
import com.aspa.aspa.features.roadmap.RoadmapListScreen
import com.aspa.aspa.features.roadmap.components.RoadmapDialog
import com.aspa.aspa.features.study.StudyScreen

object RoadmapDestinations {
//    const val ROADMAP_LIST = "roadmap/{id}"
    // todo: make route roadmap/{questionId} from question
    const val ROADMAP_LIST = "roadmap"
    const val ROADMAP_DETAIL = "roadmap/detail/{id}"
    const val ROADMAP_DIALOG = "roadmap/dialog/{id}"
    const val STUDY = "study"
    const val QUIZ = "quiz"
}

fun NavGraphBuilder.roadmapGraph(navController: NavController) {
    composable(
        route = RoadmapDestinations.ROADMAP_LIST,
//        arguments = listOf(navArgument("id") { type = NavType.StringType })
    ) {backStackEntry ->
//        val questionId = backStackEntry.arguments?.getString("id") ?: ""
        RoadmapListScreen(
            navController = navController,
//            questionId = questionId
            questionId = ""
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
        StudyScreen()
    }

    composable(RoadmapDestinations.QUIZ) {
        QuizScreen(navController = navController)
    }
}