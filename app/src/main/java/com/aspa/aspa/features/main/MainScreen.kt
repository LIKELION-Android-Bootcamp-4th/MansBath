package com.aspa.aspa.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aspa.aspa.features.home.HomeScreen
import com.aspa.aspa.features.home.HomeScreenActions
import com.aspa.aspa.features.home.HomeScreenState
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.home.components.HomeDrawerContent
import com.aspa.aspa.features.main.components.BottomNavigationBar
import com.aspa.aspa.features.main.components.DefaultTopBar
import com.aspa.aspa.features.main.components.HomeTopBar
import com.aspa.aspa.features.mypage.MyPageScreen
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.roadmap.RoadmapListScreen
import com.aspa.aspa.features.roadmap.components.RoadmapTopBar
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    homeViewModel: HomeViewModel = viewModel()
) {
    LaunchedEffect(key1 = Unit) {
        homeViewModel.initialize()
    }

    val innerNavController: NavHostController = rememberNavController()

    val currentBackStackEntry by innerNavController.currentBackStackEntryAsState()
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
                    "roadmap/{questionId}" -> RoadmapTopBar()
                    else -> DefaultTopBar()
                }
            },
            bottomBar = {
                if (currentRoute in listOf("home", "quiz", "mypage") || currentRoute?.startsWith("roadmap") == true) {
                    BottomNavigationBar(
                        currentRoute = currentRoute,
                        onTabSelected = { route -> innerNavController.navigate(route) }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = innerNavController,
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
                                uiState.activeConversationId?.let { questionId ->
                                    innerNavController.navigate("roadmap/$questionId")
                                }
                            }
                        )
                    )
                }
                composable("quiz") { QuizScreen(innerNavController) }
                composable(
                    route = "roadmap/{questionId}",
                    arguments = listOf(navArgument("questionId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val questionId = backStackEntry.arguments?.getString("questionId")
                    RoadmapListScreen(
                        innerNavController,
                        questionId = questionId,
                    )
                }

                composable("mypage") {
                    MyPageScreen()
                }
            }
        }
    }
}