package com.aspa.aspa.features.roadmap.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.aspa.aspa.features.roadmap.RoadmapDetailScreen
import com.aspa.aspa.features.roadmap.RoadmapListScreen
import com.aspa.aspa.features.roadmap.RoadmapDialog

object RoadmapDestinations {
    const val ROADMAP_GRAPH_ROUTE = "roadmap_graph"
    const val ROADMAP_LIST = "roadmap?questionId={questionId}&fromWidget={fromWidget}"
    const val ROADMAP_DETAIL = "roadmap/{roadmapId}"
    const val ROADMAP_DIALOG = "roadmap/{roadmapId}/{sectionId}"

    fun roadmapList(questionId: String = "", fromWidget: Boolean = false) =
        "roadmap?questionId=$questionId&fromWidget=$fromWidget"

    fun roadmapDetail(roadmapId: String) =
        "roadmap/$roadmapId"

    fun roadmapDialog(roadmapId: String, sectionId: Int) =
        "roadmap/$roadmapId/$sectionId"
}

fun NavGraphBuilder.roadmapGraph(navController: NavController) {
    navigation(
        startDestination = RoadmapDestinations.ROADMAP_LIST,
        route = RoadmapDestinations.ROADMAP_GRAPH_ROUTE
    ) {
        composable(
            route = RoadmapDestinations.ROADMAP_LIST,
            arguments = listOf(
                navArgument("questionId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("fromWidget") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            ),
            deepLinks = listOf(
                navDeepLink { uriPattern = "aspa://roadmap" },
                navDeepLink { uriPattern = "aspa://roadmap?fromWidget={fromWidget}" }
            ),
        ) { backStackEntry ->
            val questionId = backStackEntry.arguments?.getString("questionId") ?: ""
            val fromWidget = backStackEntry.arguments?.getBoolean("fromWidget") ?: false

            RoadmapListScreen(
                navController = navController,
                questionId = questionId,
                fromWidget = fromWidget,
            )
        }

        composable(
            route = RoadmapDestinations.ROADMAP_DETAIL,
            arguments =
                listOf(
                    navArgument("roadmapId") { type = NavType.StringType },
                )
        ) { backStackEntry ->
            val roadmapId = backStackEntry.arguments?.getString("roadmapId")

            RoadmapDetailScreen(roadmapId!!, navController)
        }

        composable(
            route = RoadmapDestinations.ROADMAP_DIALOG,
            arguments = listOf(
                navArgument("roadmapId") { type = NavType.StringType },
                navArgument("sectionId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            val roadmapId = backStackEntry.arguments?.getString("roadmapId")
            val sectionId = backStackEntry.arguments?.getInt("sectionId")
            RoadmapDialog(
                roadmapId = roadmapId!!,
                sectionId = sectionId!!,
                navController = navController
            )
        }
    }
}