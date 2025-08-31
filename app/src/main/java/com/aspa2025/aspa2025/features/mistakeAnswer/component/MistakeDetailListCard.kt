package com.aspa2025.aspa2025.features.mistakeAnswer.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aspa2025.aspa2025.ui.theme.AppSpacing

@Composable
fun MistakeDetailListCard(
    question: String,
    chosen: String,
    answer: String,
    description: String,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(AppSpacing.md),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = AppSpacing.md, vertical = AppSpacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.sm)
        ) {
            Text("문제", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface)
            Text(question, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))

            Text("내 답안", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
            Box(
                modifier = Modifier
                    .padding(AppSpacing.xs)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.error,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(AppSpacing.md),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(chosen, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onErrorContainer)
            }

            Text("정답", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
            Box(
                modifier = Modifier
                    .padding(AppSpacing.xs)
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(AppSpacing.md),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(answer, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer)
            }

            Text("해설", style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurface)
            Text(description, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
