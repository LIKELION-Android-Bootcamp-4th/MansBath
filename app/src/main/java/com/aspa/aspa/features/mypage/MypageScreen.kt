package com.aspa.aspa.features.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspa.aspa.ui.theme.AspaTheme

@Composable
fun MypageScreen(
    viewModel: MypageViewModel = viewModel(),
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToQuiz: () -> Unit = {},
    onNavigateToRoadmap: () -> Unit = {},
    onNavigateToMypage: () -> Unit = {}
) {
    val user by viewModel.currentUser.collectAsState()
    
    MypageScreenContent(
        userName = user?.displayName ?: "사용자",
        nickname = "김철수", // TODO: 실제 닉네임 데이터 연동
        onLogout = onLogout,
        onNavigateToHome = onNavigateToHome,
        onNavigateToQuiz = onNavigateToQuiz,
        onNavigateToRoadmap = onNavigateToRoadmap,
        onNavigateToMypage = onNavigateToMypage
    )
}

@Composable
private fun MypageScreenContent(
    userName: String,
    nickname: String,
    onLogout: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToRoadmap: () -> Unit,
    onNavigateToMypage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // 상단 헤더
            HeaderSection()
            
            // 메인 콘텐츠
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 24.dp)
            ) {
                // 프로필 카드
                ProfileCard(
                    nickname = nickname,
                    onEditClick = { /* TODO: 닉네임 편집 */ }
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // 로그아웃 버튼
                LogoutButton(onLogout = onLogout)
            }
        }
        
        // 하단 네비게이션 바
        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onNavigateToHome = onNavigateToHome,
            onNavigateToQuiz = onNavigateToQuiz,
            onNavigateToRoadmap = onNavigateToRoadmap,
            onNavigateToMypage = onNavigateToMypage
        )
    }
}

@Composable
private fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        // 앱 이름
        Text(
            text = "Aspa",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // 페이지 제목
        Text(
            text = "마이페이지",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        
        // 설명
        Text(
            text = "프로필과 활동 내역을 관리하세요",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ProfileCard(
    nickname: String,
    onEditClick: () -> Unit
) {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 아이콘
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nickname.firstOrNull()?.uppercase() ?: "A",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // 닉네임 정보
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "닉네임",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                Text(
                    text = nickname,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            
            // 편집 버튼
            IconButton(onClick = onEditClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "닉네임 편집",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun LogoutButton(onLogout: () -> Unit) {
    Button(
        onClick = onLogout,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE57373) // 빨간색
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ExitToApp,
            contentDescription = "로그아웃",
            tint = Color.White,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "로그아웃",
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToRoadmap: () -> Unit,
    onNavigateToMypage: () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // HOME
            NavigationItem(
                icon = Icons.Default.Home,
                label = "HOME",
                isSelected = false,
                onClick = onNavigateToHome
            )
            
            // QUIZ
            NavigationItem(
                icon = Icons.Filled.List,
                label = "QUIZ",
                isSelected = false,
                onClick = onNavigateToQuiz
            )
            
            // ROADMAP
            NavigationItem(
                icon = Icons.Filled.Star,
                label = "ROADMAP",
                isSelected = false,
                onClick = onNavigateToRoadmap
            )
            
            // MYPAGE
            NavigationItem(
                icon = Icons.Default.Person,
                label = "MYPAGE",
                isSelected = true,
                onClick = onNavigateToMypage
            )
        }
    }
}

@Composable
private fun NavigationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(40.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFFFF9800) else Color.Gray,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            fontSize = 10.sp,
            color = if (isSelected) Color(0xFFFF9800) else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MypageScreenPreview() {
    AspaTheme {
        MypageScreenContent(
            userName = "테스트 사용자",
            nickname = "김철수",
            onLogout = {},
            onNavigateToHome = {},
            onNavigateToQuiz = {},
            onNavigateToRoadmap = {},
            onNavigateToMypage = {}
        )
    }
}
}