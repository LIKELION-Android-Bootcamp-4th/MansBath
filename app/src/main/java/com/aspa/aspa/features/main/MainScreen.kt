package com.aspa.aspa.features.main

import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.home.components.HomeDrawerContent
import com.aspa.aspa.features.home.navigation.HomeDestinations
import com.aspa.aspa.features.home.navigation.homeGraph
import com.aspa.aspa.features.main.components.BottomNavigationBar
import com.aspa.aspa.features.main.components.DefaultTopBar
import com.aspa.aspa.features.main.components.HomeTopBar
import com.aspa.aspa.features.main.navigation.BottomTab
import com.aspa.aspa.features.mypage.navigation.mypageGraph
import com.aspa.aspa.features.quiz.navigation.QuizDestinations
import com.aspa.aspa.features.quiz.navigation.quizGraph
import com.aspa.aspa.features.roadmap.components.RoadmapTopBar
import com.aspa.aspa.features.roadmap.navigation.RoadmapDestinations
import com.aspa.aspa.features.roadmap.navigation.roadmapGraph
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    homeViewModel: HomeViewModel = hiltViewModel()
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
                onDeleteClick = { questionId -> homeViewModel.deleteQuestionHistory(questionId) },
                onRenameClick = { questionId, newTitle ->
                    homeViewModel.renameQuestion(
                        questionId,
                        newTitle
                    )
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                when (currentRoute) {
                    HomeDestinations.HOME -> HomeTopBar(
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onNewChatClick = { homeViewModel.createNewChat() }
                    )
                    RoadmapDestinations.ROADMAP_LIST -> RoadmapTopBar()
                    RoadmapDestinations.ROADMAP_DETAIL, RoadmapDestinations.ROADMAP_DIALOG -> {}
                    QuizDestinations.SOLVE_QUIZ, QuizDestinations.QUIZ_RESULT -> {}
                    else -> DefaultTopBar()
                }
            },
            bottomBar = {
                val bottomNavScreenRoutes = listOf(
                    BottomTab.Home.route,
                    BottomTab.Roadmap.route,
                    BottomTab.Quiz.route,
                    BottomTab.MyPage.route
                )

                val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val shouldShowBottomBar = currentRoute in bottomNavScreenRoutes
//                val shouldShowBottomBar = innerNavController.currentBackStack.value.any {
//                    it.destination.route in bottomNavScreenRoutes
//                }

                if (shouldShowBottomBar) {
                    BottomNavigationBar(
                        currentRoute = currentRoute,
                        onTabSelected = { graphRoute ->
                            innerNavController.navigate(graphRoute) {
                                popUpTo(innerNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        ) { innerPadding ->
            NavHost(
                navController = innerNavController,
                startDestination = HomeDestinations.HOME_GRAPH_ROUTE,
                modifier = Modifier.padding(innerPadding)
            ) {
                homeGraph(navController = innerNavController)
                roadmapGraph(navController = innerNavController)
                quizGraph(navController = innerNavController)
                mypageGraph(navController = innerNavController)
            }
        }
    }
}