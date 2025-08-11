package com.aspa.aspa.features.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SocialButton(text: String, onclick: () -> Unit) {
    OutlinedButton(
        onClick = onclick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(6.75.dp),
        border = BorderStroke(1.dp, color = Color.Black.copy(alpha = 0.1f)),
    ) {
        Text(text, color = Color.Black)
    }
}