package com.aspa2025.aspa2025.features.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.aspa2025.aspa2025.ui.theme.AppSpacing

@Composable
fun RenameDialog(
    currentTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newTitle by remember { mutableStateOf(currentTitle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "질문 이름 변경",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column {
                Text(
                    text = "새로운 질문 이름을 입력하세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(AppSpacing.lg))
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    singleLine = true,
                    shape = MaterialTheme.shapes.medium
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(newTitle) },
                enabled = newTitle.isNotBlank()
            ) {
                Text(
                    text = "변경",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "취소",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    )
}
