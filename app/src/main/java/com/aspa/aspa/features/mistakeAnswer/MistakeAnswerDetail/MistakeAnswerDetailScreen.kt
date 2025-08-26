package com.aspa.aspa.features.mistakeAnswer.MistakeAnswerDetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Psychology
import androidx.compose.material.icons.outlined.TrackChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aspa.aspa.data.dto.MistakeDto
import com.aspa.aspa.features.mistakeAnswer.component.MistakeDetailListCard
import com.aspa.aspa.features.state.UiState
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MistakeAnswerDetailScreen(
    state: UiState<MistakeDto>
){
    when(state){
        UiState.Idle,UiState.Loading ->{
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("오답 분석중...")
                    Spacer(modifier = Modifier.height(10.dp))
                    CircularProgressIndicator()
                }
            }
        }
        is UiState.Failure -> {
            // 에러 메시지
            Text(
                text = "불러오기 실패: ${state.message}",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        is UiState.Success -> {
            val data = state.data
            val problems = data.items
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.White)
                    .padding(10.dp)
                    .safeContentPadding()

            ) {
                Text(data.quizTitle, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 5.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Text("날짜: ${data.currentAt}", style = MaterialTheme.typography.bodyMedium)
                    Text("틀린 문제 :${data.items.size}", style = MaterialTheme.typography.bodyMedium, color = Color.Red)
                }

                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(color = Color.White)
                                .padding(10.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color.Blue.copy(0.1f))
                        ) {
                            Column(
                                modifier = Modifier
                                    .background(color = Color.White)
                                    .padding(10.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Psychology,
                                        contentDescription = null,
                                        tint = Color.Blue.copy(alpha = 0.3f),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Text("AI 오답분석", style = MaterialTheme.typography.bodyMedium, color = Color.Blue.copy(alpha = 0.3f))
                                }
                                Text("AI 분석 결과")
                                Text("이번 오답노트 분석을 통해 다음과 같은 학습 패턴을 발견했습니다.")
                                Row {
                                    Icon(
                                        imageVector = Icons.Outlined.TrackChanges,
                                        contentDescription = "실행 계획"
                                    )
                                    Text("부족한 사항",style = MaterialTheme.typography.bodyMedium)
                                }
                                Text(data.response.root_cause?:"값이 없습니다.",style = MaterialTheme.typography.bodySmall)
                                Row {
                                    Icon(
                                        imageVector = Icons.Outlined.BarChart,
                                        contentDescription = "분석"
                                    )
                                    Text("분석",style = MaterialTheme.typography.bodyMedium)
                                }
                                Text(data.response.evidence?:"값이 없습니다.",style = MaterialTheme.typography.bodySmall)
                                Row {
                                    Icon(
                                        imageVector = Icons.Outlined.Lightbulb,
                                        contentDescription = "개선 사항"
                                    )
                                    Text("개선 사항",style = MaterialTheme.typography.bodyMedium)
                                }
                                Text(data.response.action_plan?:"값이 없습니다.",style = MaterialTheme.typography.bodySmall)
                            }

                        }

                    }

                    items(problems){ p ->
                        MistakeDetailListCard(
                            question = p.question,
                            chosen = p.chosen,
                            answer = p.answer,
                            description = p.description,
                        )
                    }

                }
            }

        }
    }


}