package com.aspa.aspa.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.features.home.components.ChatContent
import com.aspa.aspa.features.home.components.InitialContent
import com.aspa.aspa.features.home.components.UserInput

data class HomeScreenState(
    val uiState: HomeUiState,
    val inputText: String
)

data class HomeScreenActions(
    val onInputTextChanged: (String) -> Unit,
    val onSendClicked: () -> Unit,
    val onOptionSelected: (String) -> Unit,
    val onRoadmapCreateClicked: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeScreenState,
    actions: HomeScreenActions
) {
    val chatStarted = state.uiState.messages.isNotEmpty()

    /**
     * 돌고돌아 원점. 스캐폴드 + 로드맵 생성 버튼 FAB.(튜닝의 최종 목적지~)
     */
    Scaffold(
        bottomBar = {
            UserInput(
                text = state.inputText,
                onTextChanged = actions.onInputTextChanged,
                onSendClicked = actions.onSendClicked
            )
        },
        floatingActionButton = {
            if (state.uiState.isReportFinished) {
                FloatingActionButton(
                    onClick = actions.onRoadmapCreateClicked,
                    shape = RoundedCornerShape(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 16.dp)
                ) {
                    Text(
                        text = "로드맵 생성",
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.uiState.isLoading && !chatStarted) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (chatStarted) {
                ChatContent(
                    messages = state.uiState.messages,
                    onOptionSelected = actions.onOptionSelected,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            } else {
                InitialContent(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}