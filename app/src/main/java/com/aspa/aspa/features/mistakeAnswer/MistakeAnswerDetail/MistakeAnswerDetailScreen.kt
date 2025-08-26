package com.aspa.aspa.features.mistakeAnswerScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MistakeAnswerDetailScreen(
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)

    ) {
        Text("$quizTitle?:제목", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text("날짜: ${currentAt}", style = MaterialTheme.typography.bodyMedium)
              Text("틀린 문제 : ${index}", style = MaterialTheme.typography.bodyMedium, color = Color.Red)
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            shape = RoundedCornerShape(10.dp),
            border = BorderStroke(1.dp, Color.Blue.copy(0.7f))
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Psychology,
                        contentDescription = null,
                        tint = Color.Blue.copy(alpha = 0.8f),
                        modifier = Modifier.size(20.dp)
                    )
                    Text("AI 오답분석", style = MaterialTheme.typography.bodyMedium, color = Color.Blue.copy(alpha = 0.8f))
                }
                Text("AI 분석 결과")
                Text("이번 오답노트 분석을 통해 다음과 같은 학습 패턴을 발견했습니다.")
                Row {
                    Icon(
                        imageVector = Icons.Outlined.TrackChanges,
                        contentDescription = "실행 계획"
                    )
                    Text("실행 계획",style = MaterialTheme.typography.bodyMedium)
                }
                ///데이터 넣기
                Row {
                    Icon(
                        imageVector = Icons.Outlined.BarChart,
                        contentDescription = "분석"
                    )
                    Text("분석",style = MaterialTheme.typography.bodyMedium)
                }
                ///데이터 넣기
                Row {
                    Icon(
                        imageVector = Icons.Outlined.Lightbulb,
                        contentDescription = "개선 사항"
                    )
                    Text("개선 사항",style = MaterialTheme.typography.bodyMedium)
                }
                ///데이터 넣기
            }

        }
    }

}