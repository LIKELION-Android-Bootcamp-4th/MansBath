package com.aspa.aspa.core.constants.enums

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class BottomTab(val label: String, val icon: ImageVector) {
    Quiz("퀴즈", Icons.Filled.Checklist),
    Home("홈", Icons.Filled.Home),
    Roadmap("로드맵", Icons.Filled.Explore),
    MyPage("마이페이지", Icons.Filled.Person)
}
