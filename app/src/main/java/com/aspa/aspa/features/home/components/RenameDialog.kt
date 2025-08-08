package com.aspa.aspa.features.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RenameDialog(
    currentTitle: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newTitle by remember { mutableStateOf(currentTitle) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("질문 이름 변경") },
        text = {
            Column {
                Text("새로운 질문 이름을 입력하세요.")
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = newTitle,
                    onValueChange = { newTitle = it },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(newTitle)
                },
                enabled = newTitle.isNotBlank()
            ) {
                Text("변경")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("취소")
            }
        }
    )
}