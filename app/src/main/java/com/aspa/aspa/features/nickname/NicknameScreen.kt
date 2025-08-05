package com.aspa.aspa.features.nickname

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspa.aspa.ui.theme.AspaTheme

@Composable
fun NicknameScreen(
    viewModel: NicknameViewModel = viewModel(),
    onPrevious: () -> Unit = {},
    onStart: () -> Unit = {}
) {
    val nicknameState by viewModel.nicknameState.collectAsState()
    val nickname by viewModel.nickname.collectAsState()
    
    // 상태에 따른 처리
    LaunchedEffect(nicknameState) {
        when (nicknameState) {
            is NicknameState.Success -> {
                onStart()
                viewModel.resetState()
            }
            is NicknameState.Error -> {
                println("닉네임 설정 실패: ${(nicknameState as NicknameState.Error).message}")
                viewModel.resetState()
            }
            else -> {}
        }
    }
    
    NicknameScreenContent(
        nickname = nickname,
        nicknameState = nicknameState,
        onNicknameChange = { viewModel.updateNickname(it) },
        onPrevious = onPrevious,
        onStart = { viewModel.saveNickname() }
    )
}

@Composable
private fun NicknameScreenContent(
    nickname: String,
    nicknameState: NicknameState,
    onNicknameChange: (String) -> Unit,
    onPrevious: () -> Unit,
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        // 메인 카드
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 12.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 뇌 아이콘 (그라데이션 배경)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF6C63FF), // 보라색
                                    Color(0xFF4FC3F7)  // 파란색
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🧠",
                        fontSize = 40.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 제목
                Text(
                    text = "닉네임을 설정해주세요",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A),
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                // 설명
                Text(
                    text = "다른 사용자들에게 보여질 닉네임입니다",
                    fontSize = 15.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(bottom = 32.dp)
                )
                
                // 닉네임 입력 필드
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "닉네임",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    OutlinedTextField(
                        value = nickname,
                        onValueChange = onNicknameChange,
                        placeholder = {
                            Text(
                                text = "닉네임을 입력하세요",
                                color = Color(0xFF999999),
                                fontSize = 15.sp
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF6C63FF),
                            unfocusedBorderColor = Color(0xFFE0E0E0),
                            focusedContainerColor = Color(0xFFFAFAFA),
                            unfocusedContainerColor = Color(0xFFFAFAFA),
                            cursorColor = Color(0xFF6C63FF)
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        singleLine = true,
                        maxLines = 1,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 15.sp,
                            color = Color(0xFF1A1A1A)
                        )
                    )
                }
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // 버튼들
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // 이전 버튼
                    Button(
                        onClick = onPrevious,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF0F0F0)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "이전",
                            color = Color(0xFF666666),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    // 시작하기 버튼
                    Button(
                        onClick = onStart,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = nickname.trim().isNotEmpty() && nicknameState !is NicknameState.Loading
                    ) {
                        Text(
                            text = "시작하기 >",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                // 로딩 상태 표시
                if (nicknameState is NicknameState.Loading) {
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator(
                        color = Color(0xFF6C63FF),
                        strokeWidth = 3.dp
                    )
                }
                
                // 에러 메시지 표시
                if (nicknameState is NicknameState.Error) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = (nicknameState as NicknameState.Error).message,
                        color = Color(0xFFE57373),
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NicknameScreenPreview() {
    AspaTheme {
        NicknameScreenContent(
            nickname = "",
            nicknameState = NicknameState.Idle,
            onNicknameChange = {},
            onPrevious = {},
            onStart = {}
        )
    }
} 