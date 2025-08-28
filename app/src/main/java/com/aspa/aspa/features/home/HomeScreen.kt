package com.aspa.aspa.features.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aspa.aspa.features.home.components.ChatContent
import com.aspa.aspa.features.home.components.InitialContent
import com.aspa.aspa.features.home.components.UserInput
import com.aspa.aspa.ui.theme.AppSpacing

data class HomeScreenState(
    val uiState: HomeUiState,
    val inputText: String
)

data class HomeScreenActions(
    val onInputTextChanged: (String) -> Unit,
    val onSendClicked: () -> Unit,
    val onOptionSelected: (String) -> Unit,
    val onRoadmapCreateClicked: (questinId: String) -> Unit,
    val onGoToRoadmapClicked: (roadmapId: String) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    actions: HomeScreenActions
) {
    val chatStarted = state.uiState.messages.isNotEmpty()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            UserInput(
                text = state.inputText,
                onTextChanged = actions.onInputTextChanged,
                onSendClicked = actions.onSendClicked
            )
        },
        floatingActionButton = {
            if (state.uiState.isReportFinished) {
                val roadmapId = state.uiState.roadmapId
                val questionId = state.uiState.questionId

                if (questionId != null) {
                    if (roadmapId.isNullOrBlank()) {
                        FloatingActionButton(
                            onClick = { actions.onRoadmapCreateClicked(questionId) },
                            shape = MaterialTheme.shapes.medium,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(end = AppSpacing.lg)
                        ) {
                            Text(
                                text = "로드맵 생성",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = AppSpacing.lg)
                            )
                        }
                    } else {
                        FloatingActionButton(
                            onClick = { actions.onGoToRoadmapClicked(roadmapId) },
                            shape = MaterialTheme.shapes.medium,
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(end = AppSpacing.lg)
                        ) {
                            Text(
                                text = "로드맵 이동",
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(horizontal = AppSpacing.lg)
                            )
                        }
                    }
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (state.uiState.isLoading && !chatStarted) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            } else if (chatStarted) {
                ChatContent(
                    messages = state.uiState.messages,
                    onOptionSelected = actions.onOptionSelected,
                    modifier = Modifier.padding(horizontal = AppSpacing.lg)
                )
            } else {
                InitialContent(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
