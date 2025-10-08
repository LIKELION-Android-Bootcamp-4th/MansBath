package com.aspa2025.aspa2025.features.mistakeAnswer.MistakeAnswer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.aspa2025.aspa2025.data.dto.MistakeSummary
import com.aspa2025.aspa2025.features.mistakeAnswer.component.MistakeListCard
import com.aspa2025.aspa2025.features.state.UiState
import com.aspa2025.aspa2025.ui.theme.AppSpacing

@Composable
fun MistakeNoteBookScreen(
    state: UiState<List<MistakeSummary>>,
    onClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .safeContentPadding()
    ) {
        when (state) {
            UiState.Idle, UiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "오답 생성중...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.height(AppSpacing.sm))
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
            is UiState.Failure -> {
                Text(
                    text = "불러오기 실패: ${state.message}",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = AppSpacing.lg, start = AppSpacing.lg, end = AppSpacing.lg)
                )
            }
            is UiState.Success -> {
                val items = state.data
                if (items.isEmpty()) {
                    Text(
                        text = "저장된 오답노트가 없어요.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = AppSpacing.lg, start = AppSpacing.lg, end = AppSpacing.lg)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = AppSpacing.md, horizontal = AppSpacing.lg),
                        verticalArrangement = Arrangement.spacedBy(AppSpacing.md)
                    ) {
                        items(items, key = { it.id }) { item ->
                            MistakeListCard(
                                quizTitle = item.quizTitle,
                                currentAt = item.currentAt,
                                index = item.itemsCount,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onClick(item.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
