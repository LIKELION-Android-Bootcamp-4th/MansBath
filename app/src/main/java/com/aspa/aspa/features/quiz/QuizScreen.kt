package com.aspa.aspa.features.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aspa.aspa.features.quiz.component.QuizListCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {
    val quizListState by viewModel.quizListState.collectAsState()
    val expandedIndexState by viewModel.expandedIndex.collectAsState()
    val lazyListState = rememberLazyListState()
    val lazyListStateIndex by viewModel.lazyListStateIndex.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getQuizzes()
        lazyListState.animateScrollToItem(index = lazyListStateIndex)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 8.dp)
            ) {

                when (val state = quizListState) {
                    QuizListState.Loading -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "퀴즈 조회 중..",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                            Spacer(Modifier.height(16.dp))
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    is QuizListState.Success -> {
                        if (state.quizzes.isEmpty()) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    "생성된 퀴즈가 없습니다.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else {
                            LazyColumn(state = lazyListState) {
                                itemsIndexed(state.quizzes) { index, item ->
                                    QuizListCard(
                                        index = index,
                                        item = item,
                                        expandedIndex = expandedIndexState,
                                        onClick = { viewModel.changeExpandedIndex(it) },
                                        navController = navController,
                                        viewModel = viewModel
                                    )
                                }
                            }
                        }
                    }

                    is QuizListState.Error -> Text(
                        "에러 발생: ${state.error}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    )
}



/*
@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    val navController = rememberNavController()
    QuizScreen(navController = navController)
}*/
