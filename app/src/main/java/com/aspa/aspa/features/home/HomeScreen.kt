package com.aspa.aspa.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@Composable
fun HomeScreen(
    state: HomeScreenState,
    actions: HomeScreenActions
) {
    val chatStarted = state.uiState.messages.isNotEmpty()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (state.uiState.isLoading && !chatStarted) {
            Box(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (chatStarted) {
            ChatContent(
                messages = state.uiState.messages,
                onOptionSelected = actions.onOptionSelected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        } else {
            InitialContent(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            )
        }

        if (state.uiState.isReportFinished) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = actions.onRoadmapCreateClicked,
                    modifier = Modifier.fillMaxWidth(0.5f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "로드맵 생성", fontSize = 16.sp)
                }
            }
        }

        UserInput(
            text = state.inputText,
            onTextChanged = actions.onInputTextChanged,
            onSendClicked = actions.onSendClicked
        )
    }
}