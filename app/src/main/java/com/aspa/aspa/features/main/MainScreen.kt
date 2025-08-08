package com.aspa.aspa.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.home.HomeScreen
import com.aspa.aspa.features.home.HomeScreenActions
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
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    var homeScreenActions by remember { mutableStateOf(HomeScreenActions()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            HomeDrawerContent(
                onCloseClick = { scope.launch { drawerState.close() } },
                onHistoryItemSelected = { title ->
                    homeScreenActions.loadChatHistory(title)
                    scope.launch { drawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                when (currentRoute) {
                    "home" -> HomeTopBar(
                        onMenuClick = { scope.launch { drawerState.open() } },
                        onNewChatClick = { homeScreenActions.createNewChat() }
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
                        onActionsReady = { actions ->
                            homeScreenActions = actions
                        }
                    )
                }
                composable("quiz") { QuizScreen() }
                composable("roadmap") { RoadmapListScreen() }
                composable("mypage") { MypageScreen() }
            }
        }
    }
}