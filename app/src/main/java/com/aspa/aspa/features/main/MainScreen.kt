package com.aspa.aspa.features.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.home.HomeScreen
import com.aspa.aspa.features.main.components.BottomNavigationBar
import com.aspa.aspa.features.main.components.DefaultTopBar
import com.aspa.aspa.features.mypage.MypageScreen
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.roadmap.RoadmapListScreen
import com.aspa.aspa.features.roadmap.components.RoadmapTopBar

@Preview
@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            when (currentRoute) {
//                "home" -> HomeTopBar() // todo: 인태님 작업 필요
                "roadmap" -> RoadmapTopBar()
                else -> DefaultTopBar()
            }
        },
        bottomBar = {
            BottomNavigationBar(
                currentRoute = currentRoute,
                onTabSelected = { route -> navController.navigate(route) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { HomeScreen() }
            composable("quiz") { QuizScreen() }
            composable("roadmap") { RoadmapListScreen() }
            composable("mypage") { MypageScreen() }
        }
    }

}