package com.aspa.aspa.features.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onTabSelected: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { onTabSelected("home") },
            icon = { Icon(Icons.Default.Home, contentDescription = "홈") },
            label = { Text("홈") }
        )
        NavigationBarItem(
            selected = currentRoute == "quiz",
            onClick = { onTabSelected("quiz") },
            icon = { Icon(Icons.Default.Checklist, contentDescription = "퀴즈") },
            label = { Text("퀴즈") }
        )
        NavigationBarItem(
            selected = currentRoute?.startsWith("roadmap") == true,
            onClick = { onTabSelected("roadmap?questionId=${""}") },
            icon = { Icon(Icons.Default.Explore, contentDescription = "로드맵") },
            label = { Text("로드맵") }
        )
        NavigationBarItem(
            selected = currentRoute == "mypage",
            onClick = { onTabSelected("mypage") },
            icon = { Icon(Icons.Default.Person, contentDescription = "마이페이지") },
            label = { Text("마이페이지") }
        )
    }
}
