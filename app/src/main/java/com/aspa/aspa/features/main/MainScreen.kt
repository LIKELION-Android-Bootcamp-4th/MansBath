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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.home.components.HomeDrawerContent
import com.aspa.aspa.features.home.navigation.homeGraph
import com.aspa.aspa.features.main.components.BottomNavigationBar
import com.aspa.aspa.features.main.components.DefaultTopBar
import com.aspa.aspa.features.main.components.HomeTopBar
import com.aspa.aspa.features.main.navigation.BottomTab
import com.aspa.aspa.features.mypage.navigation.mypageGraph
import com.aspa.aspa.features.quiz.navigation.QuizDestinations
import com.aspa.aspa.features.quiz.navigation.quizGraph
import com.aspa.aspa.features.roadmap.components.RoadmapTopBar
import com.aspa.aspa.features.roadmap.navigation.roadmapGraph
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
                    BottomTab.Home.route -> HomeTopBar(
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onNewChatClick = { homeViewModel.createNewChat() }
                    )
                    "roadmap/{questionId}" -> RoadmapTopBar() // todo: 하드코딩 제거
                    QuizDestinations.SOLVE_QUIZ -> {}
                    QuizDestinations.QUIZ_RESULT -> {}
                    else -> DefaultTopBar()
                }
            },
            bottomBar = {
                if (currentRoute in listOf(
                        BottomTab.Home.route,
                        BottomTab.Roadmap.route,
                        BottomTab.Quiz.route,
                        BottomTab.MyPage.route)) {
                    BottomNavigationBar(
                        currentRoute = currentRoute,
                        onTabSelected = { route -> innerNavController.navigate(route) }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = innerNavController,
                startDestination = BottomTab.Home.route,
                modifier = Modifier.padding(innerPadding)
            ) {
//                mainGraph(innerNavController)
                homeGraph(navController = innerNavController, homeViewModel)
                roadmapGraph(navController = innerNavController)
                quizGraph(navController = innerNavController)
                mypageGraph(navController = innerNavController)
            }
        }
    }
}