package com.aspa2025.aspa2025.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aspa2025.aspa2025.features.home.HomeUiState
import com.aspa2025.aspa2025.features.home.QuestionHistory
import com.aspa2025.aspa2025.ui.theme.AppSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDrawerContent(
    uiState: HomeUiState,
    onHistoryItemSelected: (String) -> Unit,
    onCloseClick: () -> Unit,
    onNewChatClick: () -> Unit,
    onDeleteClick: (String) -> Unit,
    onRenameClick: (String, String) -> Unit
) {
    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.sm),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCloseClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Close Drawer")
                }
                Spacer(modifier = Modifier.width(AppSpacing.lg))
                Text(
                    text = "질문 내역",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.weight(1f))
                OutlinedButton(
                    onClick = onNewChatClick,
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                    )
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "새 질문",
                        modifier = Modifier.size(AppSpacing.lg),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(AppSpacing.sm))
                    Text(
                        "새 질문",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(AppSpacing.lg),
                    verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
                ) {
                    items(
                        items = uiState.questionHistories,
                        key = { it.id }
                    ) { historyItem ->
                        QuestionHistoryItem(
                            history = historyItem,
                            onDeleteClick = { onDeleteClick(historyItem.id) },
                            onItemClick = { onHistoryItemSelected(historyItem.id) },
                            onRenameClick = { newTitle ->
                                onRenameClick(historyItem.id, newTitle)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun QuestionHistoryItem(
    history: QuestionHistory,
    onDeleteClick: () -> Unit,
    onRenameClick: (String) -> Unit,
    onItemClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showRenameDialog by remember { mutableStateOf(false) }

    if (showRenameDialog) {
        RenameDialog(
            currentTitle = history.title,
            onDismiss = { showRenameDialog = false },
            onConfirm = { newTitle ->
                onRenameClick(newTitle)
                showRenameDialog = false
            }
        )
    }

    Surface(
        shape = MaterialTheme.shapes.large, // pill 느낌 통일
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        ),
        tonalElevation = 0.dp,
        onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppSpacing.lg, vertical = AppSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (history.hasRoadmap) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Roadmap 준비됨",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = history.title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Box {
                IconButton(
                    onClick = { expanded = true },
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options"
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(
                                "삭제",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            onDeleteClick()
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(
                                "이름 변경",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        onClick = {
                            showRenameDialog = true
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
