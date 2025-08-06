package com.aspa.aspa.features.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun ChatContent(
    messages: List<UiChatMessage>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            coroutineScope.launch {
                listState.animateScrollToItem(index = messages.size - 1)
            }
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = messages,
            key = { message -> message.id }
        ) { message ->
            when (message) {
                is UiUserMessage -> UserMessage(text = message.text)
                is UiAssistantMessage -> {
                    AssistantMessage(text = message.text)
                    if (!message.options.isNullOrEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        OptionButtonList(
                            options = message.options,
                            onOptionSelected = onOptionSelected
                        )
                    }
                }
                is UiAnalysisReport -> AnalysisReportCard(report = message)
                else -> {}
            }
        }
    }
}

@Composable
fun UserMessage(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.Bottom
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.primary,
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 14.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "user Profile",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.5f))
                .padding(6.dp),
            tint = Color.Gray
        )
    }
}

@Composable
fun AssistantMessage(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Assistant Profile",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(Color.LightGray.copy(alpha = 0.5f))
                .padding(6.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.width(8.dp))
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
fun OptionButtonList(
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.End,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 44.dp), // 아이콘 + 여백 만큼 들여쓰기
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEachIndexed { index, option ->
            Button(
                onClick = { onOptionSelected(option) },
                modifier = Modifier,
                border = BorderStroke(1.dp, Color(0xFFC0C7FF)),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF4F6FF),
                    contentColor = MaterialTheme.colorScheme.primary
                ),
                contentPadding = PaddingValues(vertical = 10.dp, horizontal = 16.dp)
            ) {
                Text(text = "${index + 1}. $option")
            }
        }
    }
}

@Composable
fun AnalysisReportCard(report: UiAnalysisReport) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Text(
                text = report.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(20.dp))

            val reportItems = report.items.toList()
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                reportItems.forEach { (title, content) ->
                    AnalysisSection(title = title, content = content)
                }
            }
        }
    }
}

@Composable
private fun AnalysisSection(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}