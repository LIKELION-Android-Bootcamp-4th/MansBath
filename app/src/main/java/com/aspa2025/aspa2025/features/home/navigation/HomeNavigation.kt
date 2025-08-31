package com.aspa2025.aspa2025.features.home.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.aspa2025.aspa2025.features.home.HomeScreen
import com.aspa2025.aspa2025.features.home.HomeScreenActions
import com.aspa2025.aspa2025.features.home.HomeScreenState
import com.aspa2025.aspa2025.features.home.HomeViewModel
import com.aspa2025.aspa2025.features.roadmap.navigation.RoadmapDestinations

object HomeDestinations {
    const val HOME_GRAPH_ROUTE = "home_graph"
    const val HOME = "home"
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    navigation(
        startDestination = HomeDestinations.HOME,
        route = HomeDestinations.HOME_GRAPH_ROUTE
    ) {
        composable(HomeDestinations.HOME) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeDestinations.HOME_GRAPH_ROUTE)
            }
            val keyboardController = LocalSoftwareKeyboardController.current

            val uiState by homeViewModel.uiState.collectAsState()
            var inputText by remember { mutableStateOf("") }
            val chatStarted = uiState.messages.isNotEmpty()

            HomeScreen(
                state = HomeScreenState(
                    uiState = uiState,
                    inputText = inputText
                ),
                actions = HomeScreenActions(
                    onInputTextChanged = { newText -> inputText = newText },
                    onSendClicked = {
                        if (inputText.isNotBlank()) {
                            if (!chatStarted) {
                                homeViewModel.startNewChat(inputText)
                            } else {
                                homeViewModel.handleFollowUpQuestion(inputText)
                            }
                            inputText = ""

                            keyboardController?.hide()
                        }
                    },
                    onOptionSelected = { selectedOption ->
                        homeViewModel.selectOption(selectedOption)
                    },
                    onRoadmapCreateClicked = {
                        uiState.questionId?.let { questionId ->
                            navController.navigate(RoadmapDestinations.roadmapList(questionId))
                        }
                    },
                    onGoToRoadmapClicked = {
                        uiState.roadmapId?.let { roadmapId ->
                            navController.navigate(RoadmapDestinations.roadmapList()) { popUpTo(0) } // 백스택을 위해 목록으로 진입 후  // 기존의 질문화면은 백스택에서 제거
                            navController.navigate(RoadmapDestinations.roadmapDetail(roadmapId))  // 바로 로드맵 상세 화면 진입
                        }
                    }
                )
            )
        }
    }
}