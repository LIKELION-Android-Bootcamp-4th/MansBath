package com.aspa.aspa.features.quiz.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
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
            if (quiz.chosen == quiz.answer) correctQuestions++
        }
    }
    val score = (correctQuestions.toFloat() / totalQuestions.toFloat() * 100).toInt()

    val completedSection = quizzes.count { it.status == true }
    val allSection = quizzes.size
    val backgroundColor =
        if (completedSection == allSection) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        else MaterialTheme.colorScheme.surface

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(if (expandedIndex == index) -1 else index)
            },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (completedSection != 0 && completedSection != allSection) {
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(
                            text = "$completedSection/$allSection",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = if (expandedIndex == index) Int.MAX_VALUE else 1,
                    overflow = if (expandedIndex == index) TextOverflow.Clip else TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                if (completedSection == allSection) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(width = 20.dp, height = 20.dp)
                            )

                            Text(
                                text = "${score}점",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (completedSection == allSection || completedSection == 0) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            text = lastModifiedDate,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                val progress = completedSection.toFloat() / allSection.toFloat()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "전체 진도",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }

        if (expandedIndex == index) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(horizontal = 6.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                quizzes.forEach {
                    val tintColor =
                        if (it.status == true) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outlineVariant

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (it.status == true) {
                                    viewModel.getQuiz(roadmapId, it.quizTitle)
                                    viewModel.saveLazyListStateIndex(index)
                                    navController.navigate(QuizDestinations.QUIZ_RESULT)
                                } else {
                                    viewModel.getQuiz(roadmapId, it.quizTitle)
                                    viewModel.saveLazyListStateIndex(index)
                                    navController.navigate(QuizDestinations.SOLVE_QUIZ)
                                }
                            },
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        shape = MaterialTheme.shapes.small
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
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }

    Spacer(modifier = Modifier.height(12.dp))
}
