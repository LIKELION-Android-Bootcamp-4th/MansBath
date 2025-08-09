package com.aspa.aspa.features.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(
    onMenuClick: () -> Unit,
    onNewChatClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "Aspa",
                style = MaterialTheme.typography.titleLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(Icons.Default.Menu, contentDescription = "메뉴")
            }
        },
        actions = {
            IconButton(onClick = onNewChatClick) {
                Icon(Icons.Default.Add, contentDescription = "새 질문")
            }
        }
    )
}