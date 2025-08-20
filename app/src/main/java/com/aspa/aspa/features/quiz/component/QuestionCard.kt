package com.aspa.aspa.features.quiz.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.aspa.aspa.model.Quiz

@Composable
fun QuestionCard(
    questions: List<Quiz>, // ← 직접 모델 리스트 받기
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(questions.size) { index ->

            val q = questions[index]
            val isCorrect = q.chosen == q.answer
            val (icon, tint) = if (isCorrect) {
                Icons.Default.CheckCircle to Color(0xFF4CAF50)
            } else {
                Icons.Default.Cancel to Color.Red
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    // 문제 제목
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = tint,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("${index + 1}. ${q.question}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight(weight = 500), fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // 당신의 답
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("당신의 답: ", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        Text(
                            text = q.chosen,
                            color = tint,
                            fontWeight = FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    }

                    // 오답일 때만 정답 표시
                    if (!isCorrect) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("정답: ", fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                            Text(
                                text = q.answer,
                                color = Color(0xFF4CAF50),
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // 설명
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFECEEF2))
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
                            Text(text = q.description, style = MaterialTheme.typography.bodyMedium, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}