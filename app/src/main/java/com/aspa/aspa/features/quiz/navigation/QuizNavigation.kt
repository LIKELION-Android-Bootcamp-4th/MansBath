package com.aspa.aspa.features.quiz.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aspa.aspa.features.quiz.QuizResultScreen
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.quiz.SolveQuizScreen

/** 퀴즈 라우트 상수 */
//sealed class QuizScreenRoute(val route: String) {
//    object Quiz : QuizScreenRoute("quiz")
//    object SolveQuiz : QuizScreenRoute("quiz_solve")
//    object QuizResult : QuizScreenRoute("quiz_result")
//}

object QuizDestinations {
    const val QUIZ = "quiz"
    const val SOLVE_QUIZ = "quiz_solve"
    const val QUIZ_RESULT = "quiz_result"
}


fun NavGraphBuilder.quizGraph(navController: NavController) {
    composable(QuizDestinations.QUIZ) {
        QuizScreen(navController)
    }
    composable(QuizDestinations.SOLVE_QUIZ) {
        SolveQuizScreen(navController)
    }
    composable(QuizDestinations.QUIZ_RESULT) {
        QuizResultScreen(navController)
    }
}
