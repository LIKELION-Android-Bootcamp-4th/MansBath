package com.aspa.aspa.features.quiz.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.features.quiz.QuizViewModel
import com.aspa.aspa.features.quiz.navigation.QuizDestinations
import com.aspa.aspa.model.QuizInfo

@Composable
fun QuizListCard(
    index: Int,
    item: QuizInfo,
    expandedIndex: Int,
    onClick: (Int) -> Unit,
    navController: NavController,
    viewModel: QuizViewModel
) {
    val title = item.title
    val description = item.description
    val quizzes = item.quizzes.quiz
    val roadmapId = item.quizzes.roadmapId
    val lastModifiedDate = viewModel.formatTimestamp(item.lastModified)

    var correctQuestions = 0
    var totalQuestions = 0
    quizzes.forEach {
        for (quiz in it.questions) {
            totalQuestions++
            if(quiz.chosen == quiz.answer) correctQuestions++
        }
    }
    val score = (correctQuestions.toFloat() / totalQuestions.toFloat() * 100).toInt()

    val completedSection = quizzes.count { it.status == true }
    val allSection = quizzes.size
    val backgroundColor = if (completedSection == allSection) Color(0xFFB9F8CF) else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(if (expandedIndex == index) -1 else index)
            },
        border = BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        ),
        content = {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = title,
                        )
                    }

                    if (completedSection != 0 && completedSection != allSection) {
                        Card(
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "$completedSection/$allSection",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = description,
                        color = Color.Gray,
                        maxLines = if(expandedIndex == index) Int.MAX_VALUE else 1,
                        overflow = if(expandedIndex == index) TextOverflow.Clip else TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )

                    if (completedSection == allSection) {

                        Card {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.EmojiEvents,
                                    contentDescription = null,
                                    modifier = Modifier.size(width = 20.dp, height = 20.dp)
                                )

                                Text(
                                    text = "${score}점",
                                    modifier = Modifier.padding(6.dp)
                                )
                            }

                        }
                    }
                }

                Box(modifier = Modifier.height(20.dp))

                if (completedSection == allSection || completedSection == 0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            /*Card(
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "React",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(24.dp))

                            Text(
                                "3문제",
                                color = Color.Gray
                            )*/
                        }

                        if (completedSection == 0) {
                            Text(
                                text = "시작하기",
                                textDecoration = TextDecoration.Underline,

                            )
                        } else {
                            Text(
                                text = lastModifiedDate,
                            )
                        }
                    }
                } else {
                    val progress = completedSection.toFloat() / allSection.toFloat()

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("전체 진도")

                        Text("${(progress * 100).toInt()}%")
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }


            if (expandedIndex == index) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF3F3F5))
                        .padding(horizontal = 6.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    quizzes.forEach {
                        val tintColor = if (it.status == true) Color(0xFF2EB67D) else Color(0xFFD8D8D8)

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (it.status == true) {
                                        viewModel.getQuiz(roadmapId, it.quizTitle)
                                        navController.navigate(QuizDestinations.QUIZ_RESULT)
                                    } else {
                                        viewModel.getQuiz(roadmapId, it.quizTitle)
                                        navController.navigate(QuizDestinations.SOLVE_QUIZ)
                                    }
                                },
                            border = BorderStroke(1.dp, Color.Black.copy(0.1f)),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Circle,
                                    contentDescription = null,
                                    tint = tintColor,
                                    modifier = Modifier.size(width = 10.dp, height = 10.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = it.quizTitle,
                                    fontSize = 14.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    )

    Spacer(modifier = Modifier.height(12.dp))
}