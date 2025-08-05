package com.aspa.aspa.features.home.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar (
    onMenuClick: () -> Unit,
    onNewChatClick: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text("Aspa") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "메뉴"
                )
            }
        },
        actions = {
            IconButton(onClick = onNewChatClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "새 질문"
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}