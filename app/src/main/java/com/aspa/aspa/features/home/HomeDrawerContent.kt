package com.aspa.aspa.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeDrawerContent(
    onHistoryItemSelected: (String) -> Unit,
    onCloseClick: () -> Unit,
) {
    val questionHistory = remember {
        mutableStateListOf(*DummyData.dummyChatHistories.keys.toTypedArray())
    }

    ModalDrawerSheet {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, top = 8.dp, end = 16.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                IconButton(onClick = onCloseClick) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Close Drawer")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "질문 내역",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }

            LazyColumn(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = questionHistory,
                    key = { it }
                ) { questionTitle ->
                    QuestionHistoryItem(
                        text = questionTitle,
                        onDeleteClick = {
                            questionHistory.remove(questionTitle)
                        },
                        onItemClick = {
                            onHistoryItemSelected(questionTitle)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestionHistoryItem(
    text: String,
    onDeleteClick: () -> Unit,
    onItemClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, Color(0xFFF0F0F0)),
        tonalElevation = 0.dp,
        onClick = onItemClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = text,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Options",
                        tint = Color.LightGray
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("삭제") },
                        onClick = {
                            // TODO : 질문ID 가져와서 파이어스토어 날리기
                            onDeleteClick()
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}