package com.aspa.aspa.features.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.quiz.navigation.QuizDestinations


@Composable
fun SolveQuizScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {
    var selectedOption by remember { mutableStateOf("") }
    val quizState by viewModel.quizState.collectAsState()
    val solvingValue by viewModel.solvingValue.collectAsState()
    val currentRoadmapId by viewModel.currentRoadmapId.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.solveQuizAgain()
    }

    Scaffold(
        content = { padding->
            Column (
                modifier = Modifier.padding(14.dp)
            ) {
                BackHandler {
                    navController.navigate(QuizDestinations.QUIZ) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            inclusive = true
                        }
                    }
                }

                Text(
                    text = "← 나가기",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(QuizDestinations.QUIZ) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    inclusive = true
                                }
                            }
                        }
                        .padding(bottom = 16.dp)
                )

                when(val state = quizState) {
                    QuizState.Loading -> {
                        CircularProgressIndicator()

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("퀴즈를 불러오고 있습니다..")
                    }
                    is QuizState.Success -> {
                        val solvingNum = solvingValue + 1
                        val sizeNum = state.quiz.questions.size

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = state.quiz.quizTitle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                            )

                            Text(
                                text = "${solvingNum}/${sizeNum}",
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = { solvingNum.toFloat() / sizeNum.toFloat() },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val solvingQuiz = state.quiz.questions[solvingValue]

                                Text(solvingQuiz.question)

                                Spacer(modifier = Modifier.height(16.dp))

                                val options = solvingQuiz.options
                                options.forEach { option ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable { selectedOption = option }
                                            .height(IntrinsicSize.Min)
                                    ) {
                                        RadioButton(
                                            selected = selectedOption == option,
                                            onClick = { selectedOption = option }
                                        )

                                        val optionText = @Composable{
                                            Text(
                                                text = option,
                                                modifier = Modifier
                                                    .padding(horizontal = 6.dp)
                                            )
                                        }

                                        if (selectedOption == option) {
                                            Card(
                                                modifier = Modifier
                                                    .padding(start = 4.dp)
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(),
                                                colors = CardDefaults.cardColors(
                                                    containerColor = Color(0xFF3497F9)
                                                ),
                                                shape = RoundedCornerShape(8.75.dp)
                                            ) {
                                                Box(contentAlignment = Alignment.CenterStart,
                                                    modifier = Modifier.fillMaxSize()
                                                ) {
                                                    optionText()
                                                }

                                            }
                                        } else {
                                            optionText()
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
                            Button(
                                onClick = {
                                    viewModel.changeSolvingValue(SolvingState.PREVIOUS)
                                    selectedOption = ""
                                },
                                enabled = solvingValue != 0,
                                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("이전")
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = {
                                    if(solvingNum == sizeNum) {
                                        viewModel.changeSolvingChosen(solvingValue, selectedOption)
                                        viewModel.saveSolvedChosen(
                                            currentRoadmapId,
                                            state.quiz.quizTitle,
                                            viewModel.chosenAnswerList.value
                                        )
                                        navController.navigate(QuizDestinations.QUIZ_RESULT)
                                    }
                                    else {
                                        viewModel.changeSolvingValue(SolvingState.NEXT)
                                        viewModel.changeSolvingChosen(solvingValue, selectedOption)
                                        selectedOption = ""
                                    }
                                },
                                enabled = selectedOption.isNotEmpty(),
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3497F9)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                if(solvingNum == sizeNum) {
                                    Text("제출")
                                }
                                else Text("다음")
                            }
                        }
                    }

                    is QuizState.Error -> Text("에러 발생: ${state.error}")
                }
            }
        }
    )
}

/*
@Preview
@Composable
fun SolveQuizScreenPreview() {
    val navController = rememberNavController()
    SolveQuizScreen(navController = navController)
}*/
