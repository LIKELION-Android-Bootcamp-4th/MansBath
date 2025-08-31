package com.aspa2025.aspa2025.ui.components.MistakeNav

import android.util.Log
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.aspa2025.aspa2025.features.mistakeAnswer.MistakeAnswerDetail.MistakeAnswerDetailScreen
import com.aspa2025.aspa2025.features.mistakeAnswer.MistakeAnswer.MistakeAnswerViewModel
import com.aspa2025.aspa2025.features.mistakeAnswer.MistakeAnswer.MistakeNoteBookScreen
import com.aspa2025.aspa2025.features.mistakeAnswer.MistakeAnswerDetail.MistakeAnswerDetailViewModel

object MistakeDestinations {
    const val MISTAKE_GRAPH_ROUTE = "mistake_graph"
    const val MISTAKE_ANSWER = "mistake_answer"
    const val MISTAKE_ANSWER_DETAIL = "mistake_answer_detail/{mistakeId}"
    fun detail(mistakeId: String) = "mistake_answer_detail/$mistakeId"
}
fun NavGraphBuilder.mistakeGraph(navController: NavController) {
    navigation(
        startDestination = MistakeDestinations.MISTAKE_ANSWER,
        route = MistakeDestinations.MISTAKE_GRAPH_ROUTE
    ) {
        // 리스트 화면
        composable(route = MistakeDestinations.MISTAKE_ANSWER) {
            val vm: MistakeAnswerViewModel = hiltViewModel()
            val state = vm.listState.collectAsStateWithLifecycle().value

            LaunchedEffect(Unit) { vm.fetchList() }

            MistakeNoteBookScreen(
                state = state,
                onClick = { id ->
                    Log.d("로그","${id}")
                    navController.navigate(MistakeDestinations.detail(id))
                }
            )
        }

        composable(
            route = MistakeDestinations.MISTAKE_ANSWER_DETAIL,
            arguments = listOf(navArgument("mistakeId") { type = NavType.StringType })
        ) { entry ->
            val vm: MistakeAnswerDetailViewModel = hiltViewModel()
            val state = vm.listState.collectAsStateWithLifecycle().value

            LaunchedEffect(Unit) { vm.fetchList() }
            val mistakeId = requireNotNull(entry.arguments?.getString("mistakeId"))
            MistakeAnswerDetailScreen(
                state = state,
            )
        }
    }
}
