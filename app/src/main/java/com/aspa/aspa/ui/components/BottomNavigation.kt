package com.aspa.aspa.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.aspa.aspa.core.constants.enums.BottomTab

@Composable
fun BottomNavigation(
    selectedItem: BottomTab,
    onItemSelected: (BottomTab) -> Unit
) {
    NavigationBar {
        BottomTab.entries.forEach { item ->
            NavigationBarItem(
                selected = item == selectedItem,
                onClick = { onItemSelected(item) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                alwaysShowLabel = true // 선택되지 않아도 라벨 보이기
            )
        }
    }
}
