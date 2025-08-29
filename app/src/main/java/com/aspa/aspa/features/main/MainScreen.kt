package com.aspa.aspa.features.main

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.core.constants.enums.RedirectType
import com.aspa.aspa.features.home.HomeViewModel
import com.aspa.aspa.features.home.components.HomeDrawerContent
import com.aspa.aspa.features.home.navigation.HomeDestinations
import com.aspa.aspa.features.main.components.BottomNavigationBar
import com.aspa.aspa.features.main.components.CommonTopBar
import com.aspa.aspa.features.main.components.HomeTopBar
import com.aspa.aspa.features.main.navigation.MainNavigation
import com.aspa.aspa.features.mypage.navigation.MypageDestination
import com.aspa.aspa.features.quiz.navigation.QuizDestinations
import com.aspa.aspa.features.roadmap.navigation.RoadmapDestinations
import com.aspa.aspa.ui.components.MistakeNav.MistakeDestinations
import com.aspa.aspa.util.DoubleBackExitHandler
import kotlinx.coroutines.launch

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    rootNavController: NavHostController,
    redirect: String? = null,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val innerNavController: NavHostController = rememberNavController()
    val currentBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val uiState by homeViewModel.uiState.collectAsState()

    DoubleBackExitHandler()

    LaunchedEffect(redirect) {
        when (RedirectType.from(redirect)) {
            RedirectType.ROADMAP -> {
                innerNavController.navigate(RoadmapDestinations.roadmapList(fromWidget = false)) {
                    popUpTo(0)
                }
            }
            RedirectType.ROADMAP_STATUS -> {
                innerNavController.navigate(RoadmapDestinations.roadmapList(fromWidget = true)) {
                    popUpTo(0)
                }
            }
            RedirectType.NONE -> {}
        }
    }

    val isHomeScreen = currentRoute == HomeDestinations.HOME
    // 회면 상태 추가로 @Composable 로 변경함.
    val mainContent = @Composable {
        Scaffold(
            topBar = {
                when (currentRoute) {
                    HomeDestinations.HOME -> HomeTopBar(
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onNewChatClick = { homeViewModel.createNewChat() }
                    )
                    RoadmapDestinations.ROADMAP_LIST -> CommonTopBar("학습 로드맵", "단계별로 체계적인 학습을 진행하세요")
                    QuizDestinations.QUIZ -> CommonTopBar("퀴즈", "학습한 내용을 확인해 보세요")
                    MypageDestination.MYPAGE -> CommonTopBar("마이페이지", "프로필과 활동 내역을 관리하세요")
                    MistakeDestinations.MISTAKE_ANSWER -> CommonTopBar("오답노트","틀린문제를 다시 확인하고 학습하세요.")
                    else -> {}
                }
            },
            bottomBar = {
                val bottomNavScreenRoutes = listOf(
                    HomeDestinations.HOME,
                    RoadmapDestinations.ROADMAP_LIST,
                    QuizDestinations.QUIZ,
                    MistakeDestinations.MISTAKE_ANSWER,
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

    if (isHomeScreen) {
        BackHandler(enabled = drawerState.isOpen) {
            scope.launch { drawerState.close() }
        }

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
            mainContent()
        }
    } else {
        mainContent()
    }
}