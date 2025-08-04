package com.aspa.aspa.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.features.home.components.ChatContent
import com.aspa.aspa.features.home.components.DummyData
import com.aspa.aspa.features.home.components.InitialContent
import com.aspa.aspa.features.home.components.ModelMessage
import com.aspa.aspa.features.home.components.UiAnalysisReport
import com.aspa.aspa.features.home.components.UiAssistantMessage
import com.aspa.aspa.features.home.components.UiChatMessage
import com.aspa.aspa.features.home.components.UiUserMessage
import com.aspa.aspa.features.home.components.UserInput
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val AppColorScheme = lightColorScheme(
    /**
     * 블루 컨펌 반영
     * - 526EF6
     * - F4F4F4
     * - F4F6FF
     * - C0C7FF
     * - 526EF6
     *
     * 그린? 컨펌 반영 (컨펌 이름은 그린임 암튼 그린)
     * - 353739
     * - ECF2F5
     * - FFFFFF
     * - DDE1E3
     * - 353739
     *
     * Primary 컬러라 앱컬러에 변수명 아무렇게나 넣으려다가 멈췄습니다.
     * 폰트랑 컬러는 알아서 하라고 해서 일단 제껀 여기 위에다 추가해둡니다.
     * TODO : 폰트, 컬러 클래스 정리되면 머지할때 앱컬러에 다 때려박고 땡겨옵니다.
     */
    primary = Color(0xFF526EF6),
    onPrimary = Color.White,
    secondaryContainer = Color(0xFFF4F4F4),
    onSecondaryContainer = Color(0xFF353739),
    primaryContainer = Color(0xFFF4F6FF),
    onPrimaryContainer = Color(0xFF526EF6),
    background = Color.White,
    surface = Color.White,
    onBackground = Color.Black,
    onSurface = Color.Black
)

private fun mapModelToUiMessage(modelMessage: ModelMessage, id: String): UiChatMessage {
    return if (modelMessage.result != null) {
        UiAnalysisReport(
            id = id,
            date = null,
            title = "[사용자 분석 결과]",
            items = modelMessage.result
        )
    } else {
        UiAssistantMessage(
            id = id,
            date = null,
            text = modelMessage.message,
            options = modelMessage.choices?.ifEmpty { null }
        )
    }
}


@Composable
fun HomeScreen() {
    var isLoading by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<UiChatMessage>() }
    var currentStep by remember { mutableIntStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    val chatStarted = messages.isNotEmpty()
    val isReportFinished by remember {
        derivedStateOf { messages.lastOrNull() is UiAnalysisReport }
    }
    var followUpStep by remember { mutableIntStateOf(0) }

    fun startChat(initialQuestion: String) {
        coroutineScope.launch {
            isLoading = true
            val userMessage = UiUserMessage("user_0", null, initialQuestion)
            val assistantMessage = mapModelToUiMessage(DummyData.conversationFlow[0], "asst_0")
            val initialMessages = listOf(userMessage, assistantMessage)
            currentStep = 1
            delay(500L)
            messages.addAll(initialMessages)
            isLoading = false
        }
    }

    /**
     * UI 만 하고싶었는데 화면에 나타나는 아이템이 많아서
     * 대충 기능은 여기에 구현해봅니다. 뷰모델로 갈 녀석.
     * 더미라서 대충 흐름 따라가라고 메시지에 ID? 이름? 식별자. 도 박아놨는데 어차피 실 구현때 안씀
     * TODO : 대화 시작하면 questionID 응답 받아와서 사용자 대화창에 유지시키기.
     */
    fun handleFollowUpQuestion(question: String) {
        coroutineScope.launch {
            val userMessage = UiUserMessage("user_${messages.size}", null, question)
            messages.add(userMessage)
            delay(1000L)

            val responseIndex = followUpStep % DummyData.followUpResponses.size
            val assistantResponseModel = DummyData.followUpResponses[responseIndex]
            val assistantMessage = mapModelToUiMessage(assistantResponseModel, "follow_up_${followUpStep}")
            messages.add(assistantMessage)
            followUpStep++
        }
    }

    MaterialTheme(colorScheme = AppColorScheme) {
        Scaffold { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                if (isLoading) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                } else if (chatStarted) {
                    ChatContent(
                        messages = messages,
                        onOptionSelected = { selectedOption ->
                            coroutineScope.launch {
                                val userMessage = UiUserMessage("user_${messages.size}", null, selectedOption)
                                messages.add(userMessage)
                                delay(1000L)
                                if (currentStep < DummyData.conversationFlow.size) {
                                    val assistantMessage = mapModelToUiMessage(
                                        modelMessage = DummyData.conversationFlow[currentStep],
                                        id = "asst_$currentStep"
                                    )
                                    messages.add(assistantMessage)
                                    currentStep++
                                }
                            }
                        },
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

                if (isReportFinished) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(
                            onClick = {
                            /**
                             *  TODO: 로드맵 생성 로직 연결. 로드맵엔 질문ID 던져!.
                             */
                                      },
                            modifier = Modifier.fillMaxWidth(0.5f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(text = "로드맵 생성", fontSize = 16.sp)
                        }
                    }
                }

                UserInput(
                    text = inputText,
                    onTextChanged = { inputText = it },
                    onSendClicked = {
                        if (inputText.isNotBlank()) {
                            if (!chatStarted) {
                                startChat(inputText)
                            } else {
                                handleFollowUpQuestion(inputText)
                            }
                            inputText = ""
                        }
                    }
                )
            }
        }
    }
}