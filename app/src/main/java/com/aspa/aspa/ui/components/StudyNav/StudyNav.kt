package com.aspa.aspa.ui.components.StudyNav

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.study.StudyDetail.StudyDetailScreen
import com.aspa.aspa.features.study.StudyScreen

sealed class StudyScreenRoute(val route: String) {
    object Study : StudyScreenRoute("study")
    object StudyDetail : StudyScreenRoute("study_detail") {
//        fun createRoute(id: Int) = "study_detail/$id"
    }
}

@Composable
fun StudyNav(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = StudyScreenRoute.Study.route ){
        composable(StudyScreenRoute.Study.route) {
            StudyScreen(navController)
        }
        composable(StudyScreenRoute.StudyDetail.route) {
            StudyDetailScreen(navController)
        }
    }
}