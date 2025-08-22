package com.aspa.aspa.features.main

import android.annotation.SuppressLint
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.home.components.HomeDrawerContent
import com.aspa.aspa.features.home.navigation.HomeDestinations
import com.aspa.aspa.features.main.components.BottomNavigationBar
import com.aspa.aspa.features.main.components.DefaultTopBar
import com.aspa.aspa.features.main.components.HomeTopBar
import com.aspa.aspa.features.main.navigation.MainNavigation
import com.aspa.aspa.features.mypage.navigation.MypageDestination
import com.aspa.aspa.features.quiz.navigation.QuizDestinations
import com.aspa.aspa.features.roadmap.components.RoadmapTopBar
import com.aspa.aspa.features.roadmap.navigation.RoadmapDestinations
import com.aspa.aspa.util.DoubleBackExitHandler
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val innerNavController: NavHostController = rememberNavController()
    val currentBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val uiState by homeViewModel.uiState.collectAsState()

    DoubleBackExitHandler()

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
                    HomeDestinations.HOME,
                    RoadmapDestinations.ROADMAP_LIST,
                    QuizDestinations.QUIZ,
                    MypageDestination.MYPAGE
                )

                val shouldShowBottomBar = currentRoute in bottomNavScreenRoutes

                if (shouldShowBottomBar) {
                    BottomNavigationBar(
                        currentRoute = currentRoute,
                        onTabSelected = { graphRoute ->
                            innerNavController.navigate(graphRoute) {
                                popUpTo(0) {
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
            MainNavigation(
                rootNavController = rootNavController,
                innerNavController = innerNavController,
                innerPadding = innerPadding,
                homeViewModel = homeViewModel,
            )
        }
    }
}