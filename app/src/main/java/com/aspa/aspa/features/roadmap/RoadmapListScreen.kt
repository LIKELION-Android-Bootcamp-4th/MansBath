package com.aspa.aspa.features.roadmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.roadmap.components.RoadmapCard
import com.aspa.aspa.features.roadmap.components.RoadmapDialog
import com.aspa.aspa.features.study.StudyScreen
import com.aspa.aspa.ui.theme.AspaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapListScreen(
    navController: NavController,
    questionId: String?
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        LazyColumn {
            items(sampleRoadmaps.size) { index ->
                RoadmapCard(sampleRoadmaps[index]) {
                    navController.navigate("roadmapDetail/${sampleRoadmaps[index].title}")  // todo: title -> id
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoadmapListScreenPreview() {
    val nav = rememberNavController()

    AspaTheme {
        /**
         * wYN1b3dA0kGffMXCcv73 : 일본어를 배우고 싶어
         */
        RoadmapListScreen(questionId = "wYN1b3dA0kGffMXCcv73", navController = nav)
    }
}


@Composable
fun AppNavHost() {
    val navController = rememberNavController() // NavHostController 생성

    NavHost(
        navController = navController,
        startDestination = "roadmap"
    ) {
        composable("roadmap") {
            RoadmapListScreen(
                navController,
                "questionId"  // todo: question id
            )
        }
        composable(
            route = "roadmap/detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val roadmapId = backStackEntry.arguments?.getString("id") ?: ""
            RoadmapDetailScreen(roadmapId, navController)
        }
        composable(
            route = "roadmap/dialog/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val sectionId = backStackEntry.arguments?.getString("id") ?: ""
            RoadmapDialog(
                sectionId = sectionId,
                navController = navController,
            )
        }
        composable("quiz") {
            QuizScreen(
                navController = navController
            )
        }
    }
}