package com.aspa.aspa.features.quiz.navigation

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.aspa.aspa.features.quiz.QuizResultScreen
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.quiz.QuizViewModel
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

    navigation(
        startDestination = "${QuizDestinations.QUIZ}?roadmapId={roadmapId}",
        route = "quizGraph"
    ) {
        composable(
            route = "${QuizDestinations.QUIZ}?roadmapId={roadmapId}",
            arguments = listOf(navArgument("roadmapId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            // navController에서 부모 그래프('quizGraph')의 BackStackEntry를 가져옵니다.
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quizGraph")
            }
            // 부모 Entry를 기준으로 ViewModel을 생성하여 공유 인스턴스를 사용합니다.
            val viewModel: QuizViewModel = hiltViewModel(parentEntry)
            val roadmapId = backStackEntry.arguments?.getString("roadmapId")

            QuizScreen(navController, viewModel, roadmapId)
        }

        composable(QuizDestinations.SOLVE_QUIZ) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quizGraph")
            }
            val viewModel: QuizViewModel = hiltViewModel(parentEntry)
            SolveQuizScreen(navController, viewModel)
        }

        composable(QuizDestinations.QUIZ_RESULT) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry("quizGraph")
            }
            val viewModel: QuizViewModel = hiltViewModel(parentEntry)
            QuizResultScreen(navController, viewModel)
        }
    }
}
