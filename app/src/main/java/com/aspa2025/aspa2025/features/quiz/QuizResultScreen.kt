package com.aspa2025.aspa2025.features.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.aspa2025.aspa2025.features.quiz.component.QuestionCard
import com.aspa2025.aspa2025.features.quiz.navigation.QuizDestinations

@Composable
fun QuizResultScreen(
    navController: NavController,
    viewModel: QuizViewModel
) {
    val quizState by viewModel.quizState.collectAsState()
    val chosenAnswerList by viewModel.chosenAnswerList.collectAsState()
    val currentRoadmapId by viewModel.currentRoadmapId.collectAsState()

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp, horizontal = 24.dp)
    ) {
        BackHandler {
            navController.navigate(QuizDestinations.QUIZ) {
                popUpTo(navController.graph.findStartDestination().id) {
                    inclusive = true
                }
            }
        }

        // 상단 내비게이션
        Text(
            text = "← 퀴즈 목록",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary,
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

        // 점수 박스
        when (val state = quizState) {
            QuizState.Loading -> CircularProgressIndicator()
            is QuizState.Success -> {
                val questions = state.quiz.questions
                LaunchedEffect(chosenAnswerList) {
                    if (chosenAnswerList[0] != "") {
                        viewModel.syncChosenToQuestions()
                    }
                }

                val correctQuestions = questions.count { it.answer == it.chosen }
                val totalQuestions = questions.size
                val score = (correctQuestions.toFloat() / totalQuestions.toFloat() * 100).toInt()

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                    shape = MaterialTheme.shapes.medium
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
                            tint = MaterialTheme.colorScheme.tertiary,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "${score}점",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 20.sp,
                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                        )
                        Text(
                            "$correctQuestions/$totalQuestions 문제 정답",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                }

                // 문제별 결과
                Text(
                    "문제별 결과",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(8.dp))

                QuestionCard(
                    questions = questions,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.deleteQuiz(
                                currentRoadmapId,
                                state.quiz.quizTitle
                            )
                            viewModel.requestQuiz(
                                currentRoadmapId,
                                state.quiz.studyId,
                                state.quiz.sectionId
                            )
                            navController.navigate(QuizDestinations.SOLVE_QUIZ)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("새로운 퀴즈 풀기")
                    }

                    Button(
                        onClick = {
                            viewModel.solveQuizAgain()
                            navController.navigate(QuizDestinations.SOLVE_QUIZ)
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("다시 풀기")
                    }
                }
            }
            is QuizState.Error -> Text(
                "에러 발생: ${state.error}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
