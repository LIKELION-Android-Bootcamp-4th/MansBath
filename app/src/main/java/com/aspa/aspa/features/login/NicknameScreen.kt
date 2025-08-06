// features/login/NicknameScreen.kt

package com.aspa.aspa.features.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.R
import com.aspa.aspa.ui.theme.AspaTheme

@Composable
fun NicknameScreen(
    onNavigateToPrevious: () -> Unit,
    onNavigateToNext: (String) -> Unit
) {
    // 닉네임 입력값을 관리하는 상태
    var nickname by remember { mutableStateOf("") }

    NicknameScreenContent(
        nickname = nickname,
        onNicknameChange = { nickname = it },
        onNavigateToPrevious = onNavigateToPrevious,
        onNavigateToNext = { onNavigateToNext(nickname) }
    )
}

@Composable
private fun NicknameScreenContent(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    onNavigateToPrevious: () -> Unit,
    onNavigateToNext: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isNicknameValid = nickname.isNotBlank() // 닉네임이 비어있지 않은지 확인

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 아이콘
                Icon(
                    painter = painterResource(id = R.drawable.aspalogo), // 아이콘 리소스 필요
                    contentDescription = "닉네임 설정 아이콘",
                    modifier = Modifier.size(48.dp),
                    tint = Color.Unspecified
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 제목 및 부제목
                Text(
                    text = "닉네임을 설정해주세요",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "다른 사용자들에게 보여질 닉네임입니다",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 닉네임 입력 필드
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "닉네임",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = onNicknameChange,
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("닉네임을 입력하세요") },
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 하단 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onNavigateToPrevious,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("이전", color = Color.Gray)
                    }
                    Button(
                        onClick = onNavigateToNext,
                        modifier = Modifier.weight(1f),
                        enabled = isNicknameValid, // 닉네임이 비어있지 않을 때만 활성화
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("시작하기")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun NicknameScreenPreview() {
    AspaTheme {
        NicknameScreen(
            onNavigateToPrevious = {},
            onNavigateToNext = {}
        )
    }
}