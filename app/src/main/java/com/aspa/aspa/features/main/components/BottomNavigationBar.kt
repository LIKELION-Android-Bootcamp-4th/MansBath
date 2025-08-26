package com.aspa.aspa.features.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.aspa.aspa.features.home.navigation.HomeDestinations
import com.aspa.aspa.features.mypage.navigation.MypageDestination
import com.aspa.aspa.features.quiz.navigation.QuizDestinations
import com.aspa.aspa.features.roadmap.navigation.RoadmapDestinations
import com.aspa.aspa.ui.components.MistakeNav.MistakeDestinations

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onTabSelected: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == HomeDestinations.HOME,
            onClick = { onTabSelected(HomeDestinations.HOME_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.Home, contentDescription = "홈") },
            label = { Text("홈") }
        )
        NavigationBarItem(
            selected = currentRoute == QuizDestinations.QUIZ,
            onClick = { onTabSelected(QuizDestinations.QUIZ_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.Checklist, contentDescription = "퀴즈") },
            label = { Text("퀴즈") }
        )
        NavigationBarItem(
            selected = currentRoute == RoadmapDestinations.ROADMAP_LIST,
            onClick = { onTabSelected(RoadmapDestinations.ROADMAP_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "로드맵") },
            label = { Text("로드맵") }
        )
        NavigationBarItem(
            selected = currentRoute == MistakeDestinations.MISTAKE_ANSWER,
            onClick = { onTabSelected(MistakeDestinations.MISTAKE_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.AssignmentTurnedIn, contentDescription = "오답노트") },
            label = { Text("오답노트") }
        )
        NavigationBarItem(
            selected = currentRoute == MypageDestination.MYPAGE,
            onClick = { onTabSelected(MypageDestination.MYPAGE_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "마이페이지") },
            label = { Text("마이페이지") }
        )
    }
}
