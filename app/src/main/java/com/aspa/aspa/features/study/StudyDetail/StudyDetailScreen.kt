package com.aspa.aspa.features.study.StudyDetail

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.aspa.aspa.features.state.MakeQuizState
import com.aspa.aspa.features.state.UiState
import com.aspa.aspa.features.study.StudyViewModel
import com.aspa.aspa.model.Study
import com.aspa.aspa.ui.theme.Blue
import com.aspa.aspa.ui.theme.Gray
import com.aspa.aspa.ui.theme.Gray10

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyDetailScreen(
    uiState: UiState<Study>,
    onRetry : () -> Unit,
    navigateRoadmap: () -> Unit,
    navigateToQuiz: () -> Unit,
    viewModel: StudyViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val study = (uiState as? UiState.Success<Study>)?.data
    val sections = study?.items ?: emptyList()
    val expanded = remember { mutableStateOf<Pair<Int, Int>?>(0 to 0) }

    LaunchedEffect(Unit) {
        viewModel.makeQuizFlow.collect { event ->
            when(event) {
                MakeQuizState.Waiting -> {

                }
                is MakeQuizState.Navigate -> {
                    navigateToQuiz()
                }
            }
        }
    }

    when (uiState) {
        is UiState.Loading, UiState.Idle -> {
            // 로딩 상태일 때 로딩 인디케이터 표시
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("퀴즈 생성 중..")
                Spacer(Modifier.height(16.dp))
                CircularProgressIndicator()
            }
        }

        is UiState.Success -> {


            Scaffold(
                topBar = {
                    TopAppBar(
                        title = {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally

                            ) {
                                Text(
                                    text = study?.title.orEmpty(),
                                    style = MaterialTheme.typography.titleLarge,
                                    color = Color.Black
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White
                        )
                    )
                },
                bottomBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 16.dp,
                                end = 16.dp,
                                top = 12.dp,
                                bottom = WindowInsets.navigationBars
                                    .asPaddingValues()
                                    .calculateBottomPadding()
                            ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                                .background(Blue, shape = RoundedCornerShape(12.dp))
                                .clickable { navigateRoadmap() },
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.CheckCircle,
                                    contentDescription = "완료",
                                    modifier = Modifier.size(18.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("이 섹션 완료하기", color = Color.White)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(35.dp)
                                .border(
                                    BorderStroke(1.dp, Gray10),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .background(Color.White, shape = RoundedCornerShape(12.dp))
                                .clickable {
                                    viewModel.navigateOrMakeQuiz()
                                }
                            ,
                            contentAlignment = Alignment.Center
                        ) {
                            Text("퀴즈 풀기", color = Color.Black)
                        }
                    }
                },
                content = { padding ->
                    when (uiState) {
                        UiState.Loading -> {
                            Box(
                                Modifier.padding(padding).fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        is UiState.Failure -> {
                            Box(
                                Modifier.padding(padding).fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(uiState.message ?: "오류가 발생했어요.")
                                    Spacer(Modifier.height(12.dp))
                                    Button(onClick = onRetry) { Text("다시 시도") }
                                }
                            }
                        }

                        UiState.Idle, is UiState.Success -> {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier
                                    .padding(padding)
                                    .fillMaxSize()
                                    .background(Gray),
                                contentPadding = PaddingValues(vertical = 8.dp)
                            ) {
                                sections.forEachIndexed { sIdx, sec ->

                                    items(1, key = { subIdx -> "row-$sIdx-$subIdx" }) { subIdx ->
                                        val title = sec.title
                                        val detail = sec.content[subIdx]
                                        val isExpanded = expanded.value == Pair(sIdx, subIdx)

                                        ExpandableContentCard(
                                            index = sIdx + 1,
                                            title = title,
                                            content = detail,
                                            expanded = isExpanded,
                                            onClick = {
                                                expanded.value = if (isExpanded) null else Pair(sIdx, subIdx)
                                            }
                                        )
                                        Spacer(Modifier.height(10.dp))
                                    }

                                    item { Spacer(Modifier.height(6.dp)) } // 섹션 간 간격
                                }
                            }
                        }
                    }
                }
            )
        }

        is UiState.Failure -> Text("에러발생")
    }
}

