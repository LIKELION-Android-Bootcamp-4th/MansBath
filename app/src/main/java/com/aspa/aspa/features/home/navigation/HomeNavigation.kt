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
import com.aspa.aspa.features.main.MainScreen
import com.aspa.aspa.features.main.navigation.BottomTab
import com.aspa.aspa.features.main.navigation.MainDestinations
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.quiz.QuizViewModel
import com.aspa.aspa.features.quiz.navigation.QuizDestinations

object HomeDestinations {
    const val HOME = "home"
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    viewModel: HomeViewModel
) {
    navigation(
        startDestination = HomeDestinations.HOME,
        route = "homeGraph"
    ) {
        composable(HomeDestinations.HOME) {
            // Hilt로 새로 생성하는 대신, 전달받은 viewModel 사용
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
                            navController.navigate("roadmap/$questionId")
                        }
                    }
                )
            )
        }
    }
}