package com.aspa.aspa.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.features.home.components.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// 색상 테마 정의
private val AppColorScheme = lightColorScheme(
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

// ModelMessage를 UI용 모델로 변환하는 헬퍼 함수
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var isLoading by remember { mutableStateOf(false) }
    var inputText by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<UiChatMessage>() }
    var currentStep by remember { mutableIntStateOf(0) }
    var activeConversationTitle by remember { mutableStateOf<String?>(null) }

    val chatStarted = messages.isNotEmpty()
    val isReportFinished by remember {
        derivedStateOf { messages.lastOrNull() is UiAnalysisReport }
    }
    var followUpStep by remember { mutableIntStateOf(0) }

    fun createNewChat() {
        messages.clear()
        currentStep = 0
        followUpStep = 0
        activeConversationTitle = null
    }

    fun loadChatHistory(title: String) {
        val history = DummyData.dummyChatHistories[title] ?: return

        messages.clear()
        activeConversationTitle = title
        val mappedMessages = history.mapIndexed { index, historyItem ->
            when (historyItem) {
                is UserHistory -> {
                    UiUserMessage("history_${title}_user_$index", null, historyItem.message)
                }
                is ModelHistory -> {
                    mapModelToUiMessage(historyItem.message, "history_${title}_model_$index")
                }

                else -> {
                    throw IllegalArgumentException("Unknown history item type");
                }
            }
        }
        messages.addAll(mappedMessages)
        currentStep = history.size
    }

    fun startChat(initialQuestion: String) {
        scope.launch {
            isLoading = true
            messages.clear()
            activeConversationTitle = "새로운 질문" // 새 대화의 임시 제목
            val userMessage = UiUserMessage("user_0", null, initialQuestion)
            val assistantMessage = mapModelToUiMessage(DummyData.conversationFlow[0], "asst_0")

            val initialMessages = listOf(userMessage, assistantMessage)
            currentStep = 1
            delay(500L)
            messages.addAll(initialMessages)
            isLoading = false
        }
    }

    fun handleFollowUpQuestion(question: String) {
        scope.launch {
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
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                HomeDrawerContent(
                    /* A02 에 반영
                    onCloseClick = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                     */
                    onHistoryItemSelected = { title ->
                        loadChatHistory(title)
                        scope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Aspa") },
                        navigationIcon = {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "메뉴"
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = { createNewChat() }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "새 질문"
                                )
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    )
                }
            ) { innerPadding ->
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
                                scope.launch {
                                    val userMessage = UiUserMessage("user_${messages.size}", null, selectedOption)
                                    messages.add(userMessage)
                                    delay(1000L)

                                    // 새 대화 진행 로직
                                    if (activeConversationTitle == "새로운 질문" && currentStep < DummyData.conversationFlow.size) {
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
                                onClick = { /* TODO: 로드맵 생성 로직 연결 */ },
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
}