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
import com.aspa.aspa.ui.theme.AspaTheme

@Composable
fun HomeScreen(
    onNavigateToMypage: () -> Unit = {}
) {
    HomeScreenContent(
        onNavigateToMypage = onNavigateToMypage
    )
}

@Composable
private fun HomeScreenContent(
    onNavigateToMypage: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 앱 로고
            Text(
                text = "🧠",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // 제목
            Text(
                text = "ASPA 홈",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 설명
            Text(
                text = "환영합니다!",
                fontSize = 18.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 48.dp)
            )
            
            // 마이페이지 버튼
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
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AspaTheme {
        HomeScreenContent(
            onNavigateToMypage = {}
        )
    }
}