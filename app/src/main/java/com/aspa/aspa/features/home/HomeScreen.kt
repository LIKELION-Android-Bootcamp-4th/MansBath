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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspa.aspa.features.home.components.ChatContent
import com.aspa.aspa.features.home.components.HomeDrawerContent
import com.aspa.aspa.features.home.components.InitialContent
import com.aspa.aspa.features.home.components.UserInput
import kotlinx.coroutines.launch

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = viewModel()
) {
    val uiState by homeViewModel.uiState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var inputText by remember { mutableStateOf("") }

    val chatStarted = uiState.messages.isNotEmpty()

    MaterialTheme(colorScheme = AppColorScheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                HomeDrawerContent(
                    uiState = uiState,
                    onHistoryItemSelected = { questionId ->
                        homeViewModel.loadChatHistory(questionId)
                        scope.launch { drawerState.close() }
                    },
                    onCloseClick = {
                        scope.launch { drawerState.close() }
                    },
                    onNewChatClick = {
                        homeViewModel.createNewChat()
                        scope.launch { drawerState.close() }
                    },
                    onDeleteClick = { questionId ->
                        homeViewModel.deleteQuestionHistory(questionId)
                    }
                )
            }
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("Aspa") },
                        navigationIcon = {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Default.Menu, contentDescription = "메뉴")
                            }
                        },
                        actions = {
                            IconButton(onClick = { homeViewModel.createNewChat() }) {
                                Icon(Icons.Default.Add, contentDescription = "새 질문")
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
                    if (uiState.isLoading && !chatStarted) {
                        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    } else if (chatStarted) {
                        ChatContent(
                            messages = uiState.messages,
                            onOptionSelected = { selectedOption ->
                                homeViewModel.selectOption(selectedOption)
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

                    if (uiState.isReportFinished) {
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
                                    homeViewModel.startNewChat(inputText)
                                } else {
                                    homeViewModel.handleFollowUpQuestion(inputText)
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