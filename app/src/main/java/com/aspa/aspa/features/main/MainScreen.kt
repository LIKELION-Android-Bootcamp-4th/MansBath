package com.aspa.aspa.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.home.HomeScreen
import com.aspa.aspa.features.home.HomeScreenActions
import com.aspa.aspa.features.home.HomeScreenState
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.home.components.HomeDrawerContent
import com.aspa.aspa.features.main.components.BottomNavigationBar
import com.aspa.aspa.features.main.components.DefaultTopBar
import com.aspa.aspa.features.main.components.HomeTopBar
import com.aspa.aspa.features.mypage.MypageScreen
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.roadmap.RoadmapListScreen
import com.aspa.aspa.features.roadmap.components.RoadmapTopBar
import kotlinx.coroutines.launch

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    homeViewModel: HomeViewModel = viewModel()
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val uiState by homeViewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val chatStarted = uiState.messages.isNotEmpty()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerContent(
                uiState = uiState,
                onCloseClick = { scope.launch { drawerState.close() } },
                onHistoryItemSelected = { questionId ->
                    homeViewModel.loadChatHistory(questionId)
                    scope.launch { drawerState.close() }
                },
                onNewChatClick = {
                    homeViewModel.createNewChat()
                    scope.launch { drawerState.close() }
                },
                onDeleteClick = { questionId ->
                    homeViewModel.deleteQuestionHistory(questionId)
                },
                onRenameClick = { questionId, newTitle ->
                    homeViewModel.renameQuestion(questionId, newTitle)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                when (currentRoute) {
                    "home" -> HomeTopBar(
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onNewChatClick = { homeViewModel.createNewChat() }
                    )
                    "roadmap" -> RoadmapTopBar()
                    else -> DefaultTopBar()
                }
            },
            bottomBar = {
                if (currentRoute in listOf("home", "quiz", "roadmap", "mypage")) {
                    BottomNavigationBar(
                        currentRoute = currentRoute,
                        onTabSelected = { route -> navController.navigate(route) }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "home",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable("home") {
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
                                }
                            },
                            onOptionSelected = { selectedOption ->
                                homeViewModel.selectOption(selectedOption)
                            },
                            onRoadmapCreateClicked = {
                                // TODO: 로드맵 생성 로직 연결
                            }
                        )
                    )
                }
                composable("quiz") { QuizScreen() }
                composable("roadmap") { RoadmapListScreen() }
                composable("mypage") { MypageScreen() }
            }
        }
    }
}