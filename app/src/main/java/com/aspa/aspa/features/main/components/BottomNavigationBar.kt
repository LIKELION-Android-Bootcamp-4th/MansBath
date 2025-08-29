package com.aspa.aspa.features.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
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
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = MaterialTheme.colorScheme.background
        )

        NavigationBarItem(
            selected = currentRoute == HomeDestinations.HOME,
            onClick = { onTabSelected(HomeDestinations.HOME_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.Home, contentDescription = "홈") },
            label = { Text("홈", style = MaterialTheme.typography.labelMedium) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentRoute == QuizDestinations.QUIZ,
            onClick = { onTabSelected(QuizDestinations.QUIZ_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.Checklist, contentDescription = "퀴즈") },
            label = { Text("퀴즈", style = MaterialTheme.typography.labelMedium) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentRoute == RoadmapDestinations.ROADMAP_LIST,
            onClick = { onTabSelected(RoadmapDestinations.ROADMAP_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.Explore, contentDescription = "로드맵") },
            label = { Text("로드맵", style = MaterialTheme.typography.labelMedium) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentRoute == MistakeDestinations.MISTAKE_ANSWER,
            onClick = { onTabSelected(MistakeDestinations.MISTAKE_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.AssignmentTurnedIn, contentDescription = "오답노트") },
            label = { Text("오답노트", style = MaterialTheme.typography.labelMedium) },
            colors = itemColors
        )
        NavigationBarItem(
            selected = currentRoute == MypageDestination.MYPAGE,
            onClick = { onTabSelected(MypageDestination.MYPAGE_GRAPH_ROUTE) },
            icon = { Icon(Icons.Default.Person, contentDescription = "마이페이지") },
            label = { Text("마이페이지", style = MaterialTheme.typography.labelMedium) },
            colors = itemColors
        )
    }
}
