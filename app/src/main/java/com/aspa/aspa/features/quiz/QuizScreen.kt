package com.aspa.aspa.features.quiz

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aspa.aspa.features.quiz.component.QuizListCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {
    val expandedIndex = remember { mutableStateOf(-1) }
    val quizListState by viewModel.quizListState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getQuizzes("test-user-for-web")
    }


    Scaffold(
        /*topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            textAlign = TextAlign.Center,
                            text = "Aspa"
                        )
                    }

                },
            )
        },*/
        containerColor = Color.White,
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 8.dp)
            ) {
                when(val state = quizListState) {
                    QuizListState.Loading -> CircularProgressIndicator()
                    is QuizListState.Success ->
                        LazyColumn {
                            itemsIndexed(state.quizzes) { index, item ->
                                QuizListCard(
                                    index = index,
                                    title = item.quiz[0].studyId,
                                    description = item.quiz[0].studyId,
                                    quizzes = item.quiz,
                                    expandedIndex = expandedIndex.value,
                                    completedSection = 2,
                                    allSection = 6,
                                    onClick = { expandedIndex.value = it },
                                    navController = navController,
                                    viewModel = viewModel
                                )
                            }
                        }
                    is QuizListState.Error -> Text("에러 발생: ${state.error}")
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
