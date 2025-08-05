package com.aspa.aspa.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspa.aspa.ui.theme.AspaTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onLogout: () -> Unit = {},
    onNavigateToMypage: () -> Unit = {}
) {
    val user by viewModel.currentUser.collectAsState()
    
    HomeScreenContent(
        userName = user?.displayName ?: "사용자",
        onLogout = onLogout,
        onNavigateToMypage = onNavigateToMypage
    )
}

@Composable
private fun HomeScreenContent(
    userName: String,
    onLogout: () -> Unit,
    onNavigateToMypage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            // 헤더
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "안녕하세요!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    )
                    Text(
                        text = "${userName}님",
                        fontSize = 18.sp,
                        color = Color(0xFF666666)
                    )
                }
                
                Button(
                    onClick = onNavigateToMypage,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF0F0F0)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "마이페이지",
                        color = Color(0xFF666666),
                        fontSize = 14.sp
                    )
                }
            }
            
            // 메인 카드
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "오늘의 학습",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Text(
                        text = "AI와 함께하는 개인 맞춤 학습을 시작해보세요!",
                        fontSize = 16.sp,
                        color = Color(0xFF666666),
                        modifier = Modifier.padding(bottom = 24.dp)
                    )
                    
                    // 기능 버튼들
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { /* 퀴즈 화면으로 이동 */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6C63FF)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🧠",
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "퀴즈",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        
                        Button(
                            onClick = { /* 로드맵 화면으로 이동 */ },
                            modifier = Modifier
                                .weight(1f)
                                .height(80.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4FC3F7)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "🗺️",
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "로드맵",
                                    color = Color.White,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }
            
            // 추가 정보 카드
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "학습 통계",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("완료한 퀴즈", "0개")
                        StatItem("학습 시간", "0분")
                        StatItem("연속 학습", "0일")
                    }
                }
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF6C63FF)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color(0xFF666666)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AspaTheme {
        HomeScreenContent(
            userName = "테스트 사용자",
            onLogout = {},
            onNavigateToMypage = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenDefaultPreview() {
    AspaTheme {
        HomeScreenContent(
            userName = "사용자",
            onLogout = {},
            onNavigateToMypage = {}
        )
    }
}