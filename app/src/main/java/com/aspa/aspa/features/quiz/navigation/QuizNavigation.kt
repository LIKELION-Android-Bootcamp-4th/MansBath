package com.aspa.aspa.features.quiz.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.aspa.aspa.features.quiz.QuizResultScreen
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.quiz.QuizViewModel
import com.aspa.aspa.features.quiz.SolveQuizScreen

object QuizDestinations {
    const val QUIZ_GRAPH_ROUTE = "quiz_graph"
    const val QUIZ = "quiz"
    const val SOLVE_QUIZ = "quiz_solve"
    const val QUIZ_RESULT = "quiz_result"
}


fun NavGraphBuilder.quizGraph(navController: NavController) {

    navigation(
        startDestination = QuizDestinations.QUIZ,
        route = QuizDestinations.QUIZ_GRAPH_ROUTE
    ) {
        composable(QuizDestinations.QUIZ) { backStackEntry ->
            // navController에서 부모 그래프('quizGraph')의 BackStackEntry를 가져옵니다.
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(QuizDestinations.QUIZ_GRAPH_ROUTE)
            }
            // 부모 Entry를 기준으로 ViewModel을 생성하여 공유 인스턴스를 사용합니다.
            val viewModel: QuizViewModel = hiltViewModel(parentEntry)
            QuizScreen(navController, viewModel)
        }

        composable(QuizDestinations.SOLVE_QUIZ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(QuizDestinations.QUIZ_GRAPH_ROUTE)
            }
            val viewModel: QuizViewModel = hiltViewModel(parentEntry)
            SolveQuizScreen(navController, viewModel)
        }

        composable(QuizDestinations.QUIZ_RESULT) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(QuizDestinations.QUIZ_GRAPH_ROUTE)
            }
            val viewModel: QuizViewModel = hiltViewModel(parentEntry)
            QuizResultScreen(navController, viewModel)
        }
    }
}
