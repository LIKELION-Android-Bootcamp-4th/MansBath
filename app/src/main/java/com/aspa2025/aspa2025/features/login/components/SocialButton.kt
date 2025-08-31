package com.aspa2025.aspa2025.features.login.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.aspa2025.aspa2025.R
import com.aspa2025.aspa2025.core.constants.enums.Provider
import com.aspa2025.aspa2025.ui.components.SpeechBubble

@Composable
fun SocialButton(
    provider: Provider,
    isLastLogin: Boolean = false,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth().height(48.dp),
            shape = RoundedCornerShape(6.75.dp),
            border = BorderStroke(1.dp, color = Color.Black.copy(alpha = 0.1f)),
            colors = ButtonDefaults.buttonColors(
                containerColor = when(provider) {
                    Provider.GOOGLE -> Color.White
                    Provider.KAKAO -> Color(0xFFFEE500) // 카카오 옐로우
                    Provider.NAVER -> Color(0xFF03C75A) // 네이버 그린
                },
                contentColor = when(provider) {
                    Provider.GOOGLE -> Color(0xFF1F1F1F)
                    Provider.KAKAO -> Color.Black
                    Provider.NAVER -> Color.White
                }
            ),
            contentPadding = PaddingValues(horizontal = 16.dp),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    painter = when(provider) {
                        Provider.GOOGLE -> painterResource(id = R.drawable.google_login_logo)
                        Provider.KAKAO -> painterResource(id = R.drawable.kakao_login_logo)
                        Provider.NAVER -> painterResource(id = R.drawable.naver_login_logo)
                    },
                    contentDescription = when(provider) {
                        Provider.GOOGLE -> "구글 아이콘"
                        Provider.KAKAO -> "카카오 아이콘"
                        Provider.NAVER -> "네이버 아이콘"
                    },
                    tint = Color.Unspecified,
                    modifier = when (provider) {
                        Provider.NAVER -> Modifier.size(22.dp)
                        else -> Modifier.size(18.dp)
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = when (provider) {
                        Provider.GOOGLE -> "구글로 계속하기"
                        Provider.KAKAO -> "카카오로 계속하기"
                        Provider.NAVER -> "네이버로 계속하기"
                    },
                )
            }
        }

        if (isLastLogin) {
            SpeechBubble(
                text = "마지막 로그인 수단이에요",
                modifier = Modifier.offset(y = (40).dp)  // 아래로 내리기
            )
        }
    }
}