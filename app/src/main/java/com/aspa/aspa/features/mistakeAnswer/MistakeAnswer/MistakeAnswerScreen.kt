package com.aspa.aspa.features.mistakeAnswer.MistakeAnswer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aspa.aspa.data.dto.MistakeSummary
import com.aspa.aspa.features.mistakeAnswer.component.MistakeListCard
import com.aspa.aspa.features.state.UiState
import com.aspa.aspa.ui.theme.Gray

@Composable
fun MistakeNoteBookScreen(
    state: UiState<List<MistakeSummary>>,
    onClick: (String) -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.White)

    ) {
        when(state){
            UiState.Idle,UiState.Loading ->{
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("오답 생성중...")
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
                val items = state.data
                if(items.isEmpty()){
                    Text(
                        text = "저장된 오답노트가 없어요.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }else {
                    Text("틀린 문제들을 다시 확인하고 학습하세요", style = MaterialTheme.typography.bodyMedium, color = Gray)
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(items, key={it.id}){ item->
                            MistakeListCard(
                                quizTitle = item.quizTitle,
                                currentAt = item.currentAt,
                                index = item.itemsCount,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onClick(item.id) }
                            )
                        }
                    }

                }
            }

        }


    }
}