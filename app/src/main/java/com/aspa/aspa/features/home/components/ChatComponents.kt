package com.aspa.aspa.features.home.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.aspa.aspa.R
import com.aspa.aspa.ui.theme.AppSpacing

@Composable
fun ChatContent(
    messages: List<UiChatMessage>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    LaunchedEffect(messages.lastOrNull()) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(vertical = AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
    ) {
        items(items = messages, key = { it.id }) { message ->
            when (message) {
                is UiUserMessage -> UserMessage(text = message.text)
                is UiAssistantMessage -> {
                    AssistantMessage(text = message.text)
                    if (!message.options.isNullOrEmpty()) {
                        Spacer(Modifier.height(AppSpacing.sm))
                        OptionButtonList(
                            options = message.options,
                            onOptionSelected = onOptionSelected
                        )
                    }
                }
                is UiAnalysisReport -> AnalysisReportCard(report = message)
                is UiAssistantLoadingMessage -> AssistantLoadingBubble()
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
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.weight(1f, fill = false)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    horizontal = AppSpacing.lg,
                    vertical = AppSpacing.md
                )
            )
        }
        Spacer(Modifier.width(AppSpacing.sm))
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "user profile",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(AppSpacing.sm),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
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
            imageVector = Icons.Filled.Person,
            contentDescription = "assistant profile",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(AppSpacing.sm),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(AppSpacing.sm))
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.fillMaxWidth(0.8f)
        ) {
            Text(
                text = text,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(
                    horizontal = AppSpacing.lg,
                    vertical = AppSpacing.md
                )
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
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 44.dp), // 아바타 영역만큼 들여쓰기
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
    ) {
        options.forEachIndexed { index, option ->
            // Secondary 톤의 칩처럼 보이는 버튼
            Button(
                onClick = { onOptionSelected(option) },
                shape = MaterialTheme.shapes.large,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                border = BorderStroke(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
                ),
                contentPadding = PaddingValues(
                    vertical = AppSpacing.md,
                    horizontal = AppSpacing.lg
                )
            ) {
                Text(
                    text = "${index + 1}. $option",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}

@Composable
fun AnalysisReportCard(report: UiAnalysisReport) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)
        ),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = AppSpacing.lg,
                vertical = AppSpacing.xl
            )
        ) {
            Text(
                text = report.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(AppSpacing.lg))
            Divider()
            Spacer(Modifier.height(AppSpacing.xl))

            val reportItems = report.items.toList()
            Column(verticalArrangement = Arrangement.spacedBy(AppSpacing.xl)) {
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
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(AppSpacing.sm))
        Text(
            text = content,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** 낙관적 UI - 로딩 버블 */
@Composable
fun AssistantLoadingBubble() {
    val infinite = rememberInfiniteTransition(label = "loading_bubble_transition")
    val alpha by infinite.animateFloat(
        initialValue = 0.55f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "loading_alpha"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Bottom
    ) {
        Icon(
            imageVector = Icons.Filled.Person,
            contentDescription = "assistant profile",
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(AppSpacing.sm),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(AppSpacing.sm))
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = alpha),
            modifier = Modifier.width(120.dp)
        ) {
            TypingIndicator()
        }
    }
}

@Composable
fun TypingIndicator() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.typing_indicator))
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier
            .padding(AppSpacing.md)
            .size(48.dp)
    )
}
