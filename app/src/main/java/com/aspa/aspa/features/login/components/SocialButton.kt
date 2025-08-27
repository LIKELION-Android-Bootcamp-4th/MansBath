package com.aspa.aspa.features.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aspa.aspa.ui.components.SpeechBubble

@Composable
fun SocialButton(
    text: String,
    isLastLogin: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(6.75.dp),
            border = BorderStroke(1.dp, color = Color.Black.copy(alpha = 0.1f)),
        ) {Text(text, color = Color.Black)}

        if (isLastLogin) {
            SpeechBubble(
                text = "마지막 로그인 수단이에요",
                modifier = Modifier.offset(y = (40).dp)  // 아래로 내리기
            )
        }
    }
}