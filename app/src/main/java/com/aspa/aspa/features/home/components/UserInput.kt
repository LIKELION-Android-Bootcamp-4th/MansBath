package com.aspa.aspa.features.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun UserInput(
    text: String,
    onTextChanged: (String) -> Unit,
    onSendClicked: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        shadowElevation = 8.dp
    ) {
        TextField(
            value = text,
            onValueChange = onTextChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            placeholder = { Text("학습하고 싶은 내용을 입력하세요...") },
            trailingIcon = {
                IconButton(onClick = onSendClicked) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "보내기")
                }
            },
            shape = RoundedCornerShape(24.dp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                // 입력창의 배경색을 이미지와 유사한 연한 회색으로 설정
                focusedContainerColor = Color(0xFFF0F2F5),
                unfocusedContainerColor = Color(0xFFF0F2F5),
                // 포커스 됐을 때와 안됐을 때의 밑줄을 투명하게 처리
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )
    }
}