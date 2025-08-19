package com.aspa.aspa.features.home.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.aspa.aspa.features.home.HomeScreen
import com.aspa.aspa.features.home.HomeScreenActions
import com.aspa.aspa.features.home.HomeScreenState
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.roadmap.navigation.RoadmapDestinations

object HomeDestinations {
    const val HOME = "home"
    const val HOME_GRAPH_ROUTE = "homeGraph"
}

fun NavGraphBuilder.homeGraph(
    navController: NavController
) {
    navigation(
        startDestination = HomeDestinations.HOME,
        route = HomeDestinations.HOME_GRAPH_ROUTE
    ) {
        composable(HomeDestinations.HOME) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(HomeDestinations.HOME_GRAPH_ROUTE)
            }
            val viewModel: HomeViewModel = hiltViewModel(parentEntry)

            val uiState by viewModel.uiState.collectAsState()
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
                                viewModel.startNewChat(inputText)
                            } else {
                                viewModel.handleFollowUpQuestion(inputText)
                            }
                            inputText = ""
                        }
                    },
                    onOptionSelected = { selectedOption ->
                        viewModel.selectOption(selectedOption)
                    },
                    onRoadmapCreateClicked = {
                        uiState.activeConversationId?.let { questionId ->
                            navController.navigate(RoadmapDestinations.roadmapList(questionId))
                        }
                    }
                )
            )
        }
    }
}