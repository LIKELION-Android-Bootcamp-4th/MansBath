package com.aspa.aspa.features.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.ui.theme.AspaTheme

@Composable
fun MypageScreen(
    nickname: String = "사용자",
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToQuiz: () -> Unit = {},
    onNavigateToRoadmap: () -> Unit = {},
    onNavigateToMypage: () -> Unit = {},
    onNicknameChange: (String) -> Unit = {}
) {
    MypageScreenContent(
        nickname = nickname,
        onLogout = onLogout,
        onNavigateToHome = onNavigateToHome,
        onNavigateToQuiz = onNavigateToQuiz,
        onNavigateToRoadmap = onNavigateToRoadmap,
        onNavigateToMypage = onNavigateToMypage,
        onNicknameChange = onNicknameChange
    )
}

@Composable
private fun MypageScreenContent(
    nickname: String,
    onLogout: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToQuiz: () -> Unit,
    onNavigateToRoadmap: () -> Unit,
    onNavigateToMypage: () -> Unit,
    onNicknameChange: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var tempNickname by remember { mutableStateOf(nickname) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HeaderSection()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp)
            ) {
                if (isEditing) {
                    // 닉네임 편집 UI
                    NicknameEditCard(
                        nickname = tempNickname,
                        onNicknameChange = { tempNickname = it },
                        onSave = {
                            onNicknameChange(tempNickname)
                            isEditing = false
                        },
                        onCancel = {
                            tempNickname = nickname
                            isEditing = false
                        }
                    )
                } else {
                    // 기존 프로필 카드
                    ProfileCard(
                        nickname = nickname,
                        onEditClick = { isEditing = true }
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                LogoutButton(onLogout = onLogout)
            }
        }
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "마이페이지",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF333333)
        )
        Text(
            text = "Aspa",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color(0xFF666666)
        )
    }
}

@Composable
private fun ProfileCard(
    nickname: String,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
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
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 아바타 (회색 원형 배경에 "A" 텍스트)
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE0E0E0)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "A",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
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
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = nickname,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = onEditClick,
                        modifier = Modifier.size(20.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Edit,
                            contentDescription = "Edit Nickname",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NicknameEditCard(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
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
                .padding(20.dp)
        ) {
            // 제목
            Text(
                text = "닉네임 변경",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 입력 필드
            OutlinedTextField(
                value = nickname,
                onValueChange = onNicknameChange,
                placeholder = {
                    Text(
                        text = "새로운 닉네임을 입력하세요",
                        color = Color(0xFF999999),
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF6C63FF),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = Color(0xFF6C63FF)
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text
                ),
                singleLine = true,
                maxLines = 1,
                textStyle = androidx.compose.ui.text.TextStyle(
                    fontSize = 16.sp,
                    color = Color(0xFF1A1A1A),
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Left
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 버튼들
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                                 Button(
                     onClick = onCancel,
                     modifier = Modifier
                         .weight(1f)
                         .height(32.dp), // 닉네임 설정 화면과 동일한 높이
                     colors = ButtonDefaults.buttonColors(
                         containerColor = Color(0xFFF0F0F0)
                     ),
                     shape = RoundedCornerShape(8.dp)
                 ) {
                     Text(
                         text = "취소",
                         color = Color(0xFF666666),
                         fontSize = 11.sp, // 닉네임 설정 화면과 동일한 폰트 크기
                         fontWeight = FontWeight.Medium
                     )
                 }
                 
                 Button(
                     onClick = onSave,
                     modifier = Modifier
                         .weight(1f)
                         .height(32.dp), // 닉네임 설정 화면과 동일한 높이
                     colors = ButtonDefaults.buttonColors(
                         containerColor = Color(0xFF6C63FF)
                     ),
                     shape = RoundedCornerShape(8.dp),
                     enabled = nickname.trim().isNotEmpty()
                 ) {
                     Text(
                         text = "저장",
                         color = Color.White,
                         fontSize = 11.sp, // 닉네임 설정 화면과 동일한 폰트 크기
                         fontWeight = FontWeight.Medium
                     )
                 }
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
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE53935)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "로그아웃",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(
                imageVector = Icons.Filled.ArrowForward,
                contentDescription = "Logout",
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }
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
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationItem(
                icon = Icons.Filled.Star,
                label = "HOME",
                isSelected = false,
                onClick = onNavigateToHome
            )
            NavigationItem(
                icon = Icons.Filled.List,
                label = "QUIZ",
                isSelected = false,
                onClick = onNavigateToQuiz
            )
            NavigationItem(
                icon = Icons.Filled.Star,
                label = "ROADMAP",
                isSelected = false,
                onClick = onNavigateToRoadmap
            )
            NavigationItem(
                icon = Icons.Filled.Star,
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
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFFFF9800) else Color.Gray,
                modifier = Modifier.size(22.dp)
            )
        }
        Text(
            text = label,
            fontSize = 9.sp,
            color = if (isSelected) Color(0xFFFF9800) else Color.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun MypageScreenPreview() {
    AspaTheme {
        MypageScreenContent(
            nickname = "김철수",
            onLogout = {},
            onNavigateToHome = {},
            onNavigateToQuiz = {},
            onNavigateToRoadmap = {},
            onNavigateToMypage = {},
            onNicknameChange = {}
        )
    }
}