package com.aspa.aspa.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aspa.aspa.R

@Composable
fun SpeechBubble(
    text: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = colorResource(id = R.color.red_orange),
    textColor: Color = Color.White
) {
    Box(
        modifier = modifier
            .wrapContentSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 8.dp, vertical = 5.dp)
        ) {
            Text(
                text = text,
                color = textColor,
                style = MaterialTheme.typography.labelSmall
            )
        }

        // 삼각형 꼬리
        Canvas(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = (-6).dp) // 삼각형 아래로 6만큼 내리기
                .size(16.dp, 8.dp)
        ) {
            val path = Path().apply {
                moveTo(size.width / 2f, 0f) // 꼭짓점 (위 중앙)
                lineTo(0f, size.height)     // 왼쪽 아래
                lineTo(size.width, size.height) // 오른쪽 아래
                close()
            }
            drawPath(path = path, color = backgroundColor)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpeechBubble() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        SpeechBubble(text = "마지막 로그인 수단이에요")
    }
}