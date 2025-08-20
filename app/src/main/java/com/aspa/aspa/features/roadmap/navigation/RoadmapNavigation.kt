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
    const val ROADMAP_DETAIL = "roadmap/{roadmapId}"
    const val ROADMAP_DIALOG = "roadmap/{roadmapId}/{sectionId}"
    const val STUDY = "study"

    fun roadmapList(questionId: String = "") =
        "roadmap?questionId=$questionId"

    fun roadmapDetail(roadmapId: String) =
        "roadmap/$roadmapId"

    fun roadmapDialog(roadmapId: String,sectionId: Int) =
        "roadmap/$roadmapId/$sectionId"
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
        arguments = listOf(navArgument("roadmapId") { type = NavType.StringType })
    ) { backStackEntry ->
        val roadmapId = backStackEntry.arguments?.getString("roadmapId")
        RoadmapDetailScreen(roadmapId!!, navController)
    }

    composable(
        route = RoadmapDestinations.ROADMAP_DIALOG,
        arguments = listOf(navArgument("sectionId") { type = NavType.StringType })
    ) { backStackEntry ->
        val roadmapId = backStackEntry.arguments?.getString("roadmapId")
        val sectionId = backStackEntry.arguments?.getInt("sectionId")
        RoadmapDialog(
            roadmapId = roadmapId!!,
            sectionId = sectionId!!,
            navController = navController
        )
    }

    composable(RoadmapDestinations.STUDY) {
//        StudyScreen()  // todo: 스터디 연동
    }
}