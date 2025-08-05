package com.aspa.aspa.features.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.R
import com.aspa.aspa.ui.theme.AspaTheme
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.Image

@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit = {},
    onKakaoSignInClick: () -> Unit = {},
    onNaverSignInClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    LoginScreenContent(
        onGoogleSignInClick = onGoogleSignInClick,
        onKakaoSignInClick = onKakaoSignInClick,
        onNaverSignInClick = onNaverSignInClick,
        onLoginClick = onLoginClick
    )
}

@Composable
private fun LoginScreenContent(
    onGoogleSignInClick: () -> Unit,
    onKakaoSignInClick: () -> Unit,
    onNaverSignInClick: () -> Unit,
    onLoginClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(14.dp),
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
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 앱 로고 (그라데이션 원형 배경)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF6C63FF),
                                    Color(0xFF4FC3F7)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.aspalogo),
                        contentDescription = "Aspa Logo",
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 앱 이름
                Text(
                    text = "Aspa",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // 태그라인
                Text(
                    text = "AI와 함께하는 개인 맞춤 학습",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // 소셜 로그인 버튼들
                SocialLoginButton(
                    icon = R.drawable.ic_google,
                    text = "Google로 계속하기",
                    backgroundColor = Color.White,
                    textColor = Color.Black,
                    borderColor = Color(0xFFE0E0E0),
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = onGoogleSignInClick
                )
                SocialLoginButton(
                    icon = R.drawable.ic_kakao,
                    text = "카카오톡으로 계속하기",
                    backgroundColor = Color(0xFFFEE500),
                    textColor = Color(0xFF191919),
                    borderColor = Color(0xFFFEE500),
                    modifier = Modifier.padding(bottom = 8.dp),
                    onClick = onKakaoSignInClick
                )
                SocialLoginButton(
                    icon = R.drawable.ic_naver,
                    text = "네이버로 계속하기",
                    backgroundColor = Color(0xFF03C75A),
                    textColor = Color.White,
                    borderColor = Color(0xFF03C75A),
                    modifier = Modifier.padding(bottom = 16.dp),
                    onClick = onNaverSignInClick
                )

                // 메인 로그인 버튼
                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF333333)
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "로그인",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun SocialLoginButton(
    icon: Int,
    text: String,
    backgroundColor: Color,
    textColor: Color,
    borderColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(40.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                modifier = Modifier.size(16.dp),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = textColor,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun LoginScreenPreview() {
    AspaTheme {
        LoginScreenContent(
            onGoogleSignInClick = {},
            onKakaoSignInClick = {},
            onNaverSignInClick = {},
            onLoginClick = {}
        )
    }
}