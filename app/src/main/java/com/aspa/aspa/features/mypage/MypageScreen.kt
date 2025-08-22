package com.aspa.aspa.features.mypage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.aspa.aspa.features.login.navigation.LoginDestinations
import com.aspa.aspa.ui.theme.Gray10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyPageScreen(rootNavController: NavHostController, innerNavController: NavHostController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Aspa", style = MaterialTheme.typography.titleLarge)

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    Color.White
                ),


                )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .background(Color.White)
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {

                    Text(
                        "마이페이지", style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Text(
                        "프로필과 활동 내역을 관리하세요",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    color = Color.Black.copy(alpha = 0.2f)
                )

                //프로필 설정
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp, horizontal = 12.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Gray10),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 15.dp, vertical = 15.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "이미지 아이콘",
                            modifier = Modifier.size(60.dp),
                            tint = Color.Black
                        )
                        Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                            Text(
                                "닉네임",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 15.sp,
                                color = Color.Black
                            )
                            Text(
                                "닉네임 설정해주세요",
                                style = MaterialTheme.typography.bodyMedium,
                                fontSize = 15.sp,
                                color = Color.Gray
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))

                        Box(
                            modifier = Modifier
                                .background(color = Color.White, shape = RoundedCornerShape(5.dp))
                                .border(
                                    width = 1.dp,
                                    shape = RoundedCornerShape(5.dp),
                                    color = Color.Black.copy(alpha = 0.1f)
                                )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Edit,
                                contentDescription = "수정",
                                modifier = Modifier
                                    .padding(8.dp)
                                    .size(20.dp),
                                tint = Color.Black,
                            )

                        }
                    }
                }
                // 로그아웃 버튼
                Button(
                    onClick = {
                        rootNavController.navigate(LoginDestinations.LOGIN_GRAPH_ROUTE) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 15.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Logout,
                            contentDescription = "로그아웃",
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "로그아웃",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    )

}