package com.aspa.aspa.features.quiz

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.quiz.component.QuizListCard
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    navController: NavController,
    viewModel: QuizViewModel,
    roadmapId: String?
) {
    val expandedIndex = remember { mutableStateOf(-1) }
    val quizListState by viewModel.quizListState.collectAsState()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if(roadmapId != null && roadmapId != "") {
            // viewModel.requestQuizFromRoadmap(roadmapId)
            Toast.makeText(context, "퀴즈 생성중입니다. 기다려주세요..", Toast.LENGTH_SHORT).show()
        }
        else {
            viewModel.getQuizzes()
        }
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
                Text(
                    modifier = Modifier.padding(vertical = 4.dp),
                    text = "퀴즈",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                )

                Text(
                    text = "학습한 내용을 확인해 보세요.",
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(12.dp))

                when(val state = quizListState) {
                    QuizListState.Loading -> CircularProgressIndicator()
                    is QuizListState.Success ->
                        LazyColumn {
                            itemsIndexed(state.quizzes) { index, item ->
                                QuizListCard(
                                    index = index,
                                    item = item,
                                    expandedIndex = expandedIndex.value,
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
