package com.aspa.aspa.features.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun QuizResultScreen() {
    val totalQuestions = 3

    val answers = listOf(
        Triple(
            "React에서 컴포넌트의 상태를 관리하기 위해 사용하는 Hook은?",
            "useState",
            "useState는 함수형 컴포넌트에서 상태를 관리하기 위한 기본적인 Hook입니다."
        ),
        Triple(
            "JSX에서 JavaScript 표현식을 사용하려면?",
            "{ }",
            "JSX에서는 중괄호 { }를 사용하여 JavaScript 표현식을 작성합니다."
        ),
        Triple(
            "useEffect Hook의 두 번째 인자로 빈 배열 []을 전달하면?",
            "컴포넌트 마운트 시에만 실행",
            "useEffect의 의존성 배열에 빈 배열을 넣는 컴포넌트가 마운트될 때만 실행됩니다."
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 상단 내비게이션
        Text(
            text = "← 퀴즈 목록",
            modifier = Modifier
                .clickable { }
                .padding(bottom = 16.dp)
        )

        // 점수 박스
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, Color.Gray),
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = Color(0xFFFFCF00),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("100점", style = MaterialTheme.typography.headlineMedium)
                Text("$totalQuestions/$totalQuestions 문제 정답")
            }
        }

        // 문제별 결과
        Text("문제별 결과", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        answers.forEachIndexed { index, (question, answer, explanation) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${index + 1}. $question", fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("당신의 답: ", fontWeight = FontWeight.SemiBold)
                        Text(
                            text = answer,
                            color =  Color(0xFF4CAF50),
                            fontWeight = FontWeight.Medium
                        )
                    }


                    Spacer(modifier = Modifier.height(4.dp))

                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFECEEF2)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                tint = Color(0xFFFFCF00),
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = explanation)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(
                onClick = {},
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("새로운 퀴즈 풀기")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {},
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("다시 풀기")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultScreenReview() {
    QuizResultScreen()
}