package com.aspa.aspa.features.roadmap

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aspa.aspa.core.constants.enums.BottomTab
import com.aspa.aspa.features.quiz.QuizScreen
import com.aspa.aspa.features.roadmap.components.RoadmapCard
import com.aspa.aspa.features.roadmap.components.RoadmapDialog
import com.aspa.aspa.features.study.StudyScreen
import com.aspa.aspa.ui.components.BottomNavigation
import com.aspa.aspa.ui.theme.AspaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapListScreen(navController: NavController) {
    var selectedTab by remember { mutableStateOf(BottomTab.Home) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 12.dp,)
            ) {
                Text(
                    text = "Aspa",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    "학습 로드맵",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                Text(
                    "단계별로 체계적인 학습을 진행하세요",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(thickness = 1.dp, color = Color.Black.copy(alpha = 0.1f))
            }
        },
        bottomBar = {
            BottomNavigation(
                selectedItem = selectedTab,
                onItemSelected = { selectedTab = it }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
            .padding(innerPadding)
            .padding(16.dp)
        ) {

            LazyColumn {
                items(sampleRoadmaps.size) { index ->
                    RoadmapCard(sampleRoadmaps[index]) {
                        navController.navigate("roadmapDetail/${sampleRoadmaps[index].title}")  // todo: title -> id
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoadmapListScreenPreview() {
    val nav = rememberNavController()
    AspaTheme {
        RoadmapListScreen(nav)
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController() // NavHostController 생성

    NavHost(
        navController = navController,
        startDestination = "roadmap"
    ) {
        composable("roadmap") {
            RoadmapListScreen(navController)
        }
        composable(
            route = "roadmap/detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val roadmapId = backStackEntry.arguments?.getString("id") ?: ""
            RoadmapDetailScreen(roadmapId, navController)
        }
        composable(
            route = "roadmap/dialog/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val sectionId = backStackEntry.arguments?.getString("id") ?: ""
            RoadmapDialog(
                sectionId = sectionId,
                navController = navController,
            )
        }
        composable("study") {
            StudyScreen()
        }
        composable("quiz") {
            QuizScreen()
        }
    }
}