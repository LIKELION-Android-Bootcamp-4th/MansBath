package com.aspa.aspa.features.home.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.aspa.aspa.features.home.HomeScreen
import com.aspa.aspa.features.home.HomeScreenActions
import com.aspa.aspa.features.home.HomeScreenState
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.main.navigation.BottomTab
import com.aspa.aspa.features.main.navigation.MainDestinations


fun NavGraphBuilder.homeGraph(
    navController: NavController,
    homeViewModel: HomeViewModel
) {
    composable("home") {

        val uiState by homeViewModel.uiState.collectAsState()
        var inputText by remember { mutableStateOf("") }
        val chatStarted = uiState.messages.isNotEmpty()

        HomeScreen(
            state = HomeScreenState(
                uiState = uiState,
                inputText = inputText
            ), actions = HomeScreenActions(
                onInputTextChanged = { newText -> inputText = newText },
                onSendClicked = {
                    if (inputText.isNotBlank()) {
                        if (!chatStarted) {
                            homeViewModel.startNewChat(inputText)
                        } else {
                            homeViewModel.handleFollowUpQuestion(inputText)
                        }
                        inputText = ""
                    }
                },
                onOptionSelected = { selectedOption ->
                    homeViewModel.selectOption(selectedOption)
                },
                onRoadmapCreateClicked = {
                    uiState.activeConversationId?.let { questionId ->
                        navController.navigate("roadmap/$questionId")
                    }
                }
            ))
    }
}
