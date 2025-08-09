package com.aspa.aspa.ui.components.QuizNav

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.quiz.QuizResultScreen
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.quiz.SolveQuizScreen

sealed class QuizScreenRoute(val route: String) {
    object Quiz: QuizScreenRoute("quiz")
    object SolveQuiz: QuizScreenRoute("quiz_solve")
    object QuizResult: QuizScreenRoute("quiz_result")
}

@Composable
fun QuizNav() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = QuizScreenRoute.Quiz.route ){
        composable(QuizScreenRoute.Quiz.route) {
            QuizScreen(navController)
        }
        composable(QuizScreenRoute.SolveQuiz.route) {
            SolveQuizScreen(navController)
        }
        composable(QuizScreenRoute.QuizResult.route) {
            QuizResultScreen(navController)
        }
    }
}

@Composable
@Preview
fun QuizNavPreview() {
    QuizNav()
}