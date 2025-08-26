package com.aspa.aspa.features.mypage.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun ConfirmDialog(
    text: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text) },
        text = { Text("정말로 $text 하시겠습니까?") },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
                onDismiss()
            }) {
                Text("예")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("아니오")
            }
        }
    )
}
