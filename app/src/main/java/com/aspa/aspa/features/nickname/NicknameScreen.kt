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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.ui.theme.AspaTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.aspa.aspa.R

@Composable
fun NicknameScreen(
    nickname: String = "",
    onNicknameChange: (String) -> Unit = {},
    onPrevious: () -> Unit = {},
    onStart: () -> Unit = {}
) {
    NicknameScreenContent(
        nickname = nickname,
        onNicknameChange = onNicknameChange,
        onPrevious = onPrevious,
        onStart = onStart
    )
}

@Composable
private fun NicknameScreenContent(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    onPrevious: () -> Unit,
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // 배경색 변경
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp) // 카드 좌우 여백 조정
                .align(Alignment.Center), // 카드를 중앙에 정렬
            shape = RoundedCornerShape(12.dp), // 카드 모서리 둥글기
            colors = CardDefaults.cardColors(
                containerColor = Color.White // 카드 배경색
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp // 카드 그림자 강도
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp), // 카드 내부 여백
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp) // 로고 크기 조정
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF6C63FF), // 그라데이션 시작색
                                    Color(0xFF4FC3F7)  // 그라데이션 끝색
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.aspalogo),
                        contentDescription = "Aspa Logo",
                        modifier = Modifier.size(24.dp) // 로고 이미지 크기
                    )
                }

                Spacer(modifier = Modifier.height(12.dp)) // 로고와 제목 간격

                Text(
                    text = "닉네임을 설정해주세요",
                    fontSize = 14.sp, // 제목 폰트 크기
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A1A), // 제목 색상
                    modifier = Modifier.padding(bottom = 4.dp) // 제목 하단 여백
                )

                Text(
                    text = "다른 사용자들에게 보여질 닉네임입니다",
                    fontSize = 10.sp, // 설명 폰트 크기
                    color = Color(0xFF666666), // 설명 색상
                    modifier = Modifier.padding(bottom = 16.dp) // 설명 하단 여백
                )

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {

                                         OutlinedTextField(
                         value = nickname,
                         onValueChange = onNicknameChange,
                         placeholder = {
                             Text(
                                 text = "닉네임을 입력하세요",
                                 color = Color(0xFF999999), // 플레이스홀더 색상
                                 fontSize = 16.sp // 플레이스홀더 폰트 크기
                             )
                         },
                         modifier = Modifier
                             .fillMaxWidth()
                             .height(60.dp), // 입력 필드 높이 조정
                         shape = RoundedCornerShape(8.dp), // 입력 필드 모서리 둥글기
                         colors = OutlinedTextFieldDefaults.colors(
                             focusedBorderColor = Color(0xFF6C63FF), // 포커스 시 테두리 색상
                             unfocusedBorderColor = Color(0xFFE0E0E0), // 비포커스 시 테두리 색상
                             focusedContainerColor = Color.White, // 포커스 시 배경색
                             unfocusedContainerColor = Color.White, // 비포커스 시 배경색
                             cursorColor = Color(0xFF6C63FF), // 커서 색상
                             focusedLabelColor = Color(0xFF6C63FF), // 포커스 시 라벨 색상
                             unfocusedLabelColor = Color(0xFF666666) // 비포커스 시 라벨 색상
                         ),
                         keyboardOptions = KeyboardOptions(
                             keyboardType = KeyboardType.Text
                         ),
                         singleLine = true,
                         maxLines = 1,
                         textStyle = androidx.compose.ui.text.TextStyle(
                             fontSize = 18.sp, // 입력 텍스트 폰트 크기
                             color = Color(0xFF1A1A1A), // 입력 텍스트 색상
                             lineHeight = 32.sp, // 커서 세로 중앙 정렬을 위한 줄 높이 조정
                             textAlign = TextAlign.Left // 텍스트 왼쪽 정렬
                         )
                     )
                }

                Spacer(modifier = Modifier.height(20.dp)) // 입력 필드와 버튼 간격

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp) // 버튼 간격
                ) {
                    Button(
                        onClick = onPrevious,
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp), // 버튼 높이 조정
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF0F0F0) // 이전 버튼 배경색
                        ),
                        shape = RoundedCornerShape(8.dp) // 버튼 모서리 둥글기
                    ) {
                        Text(
                            text = "이전",
                            color = Color(0xFF666666), // 이전 버튼 텍스트 색상
                            fontSize = 11.sp, // 버튼 텍스트 폰트 크기
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Button(
                        onClick = onStart,
                        modifier = Modifier
                            .weight(1f)
                            .height(32.dp), // 버튼 높이 조정
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF) // 시작하기 버튼 배경색
                        ),
                        shape = RoundedCornerShape(8.dp), // 버튼 모서리 둥글기
                        enabled = nickname.trim().isNotEmpty()
                    ) {
                        Text(
                            text = "시작하기 >",
                            color = Color.White, // 시작하기 버튼 텍스트 색상
                            fontSize = 11.sp, // 버튼 텍스트 폰트 크기
                            fontWeight = FontWeight.Medium
                        )
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
        NicknameScreenContent(
            nickname = "",
            onNicknameChange = {},
            onPrevious = {},
            onStart = {}
        )
    }
} 