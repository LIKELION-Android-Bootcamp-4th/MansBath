package com.aspa.aspa.features.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.model.Section


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen() {
    val expandedIndex = remember { mutableStateOf(-1) }
    val dummyRoadmapList = DummySection.dummyRoadmapList

    Scaffold(
        topBar = {
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

                }
            )
        },
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

                LazyColumn {
                   items(dummyRoadmapList.size) { index ->
                        QuizListCard(
                            index,
                            dummyRoadmapList[index].title,
                            dummyRoadmapList[index].description,
                            dummyRoadmapList[index].sections,
                            expandedIndex.value,
                            dummyRoadmapList[index].completedSection,
                            dummyRoadmapList[index].allSection,
                            { expandedIndex.value = it }
                        )
                    }
                }

                /* QuizListCard(
                     1,
                     "React 완전 정복",
                     "React 기초부터!",
                     listOf("JavaScript 기초", "JavaScript 기초"),
                     expandedIndex.value,
                     2,
                     6,
                     { expandedIndex.value = it }
                 )

                 QuizListCard(
                     2,
                     "React 완전 정복",
                     "React 기초부터!",
                     listOf("JavaScript 기초", "JavaScript 기초"),
                     expandedIndex.value,
                     4,
                     6,
                     { expandedIndex.value = it }
                 )

                 QuizListCard(
                     3,
                     "React 완전 정복",
                     "React 기초부터!",
                     listOf("JavaScript 기초", "JavaScript 기초"),
                     expandedIndex.value,
                     0,
                     6,
                     { expandedIndex.value = it }
                 )

                 QuizListCard(
                     4,
                     "React 완전 정복",
                     "React 기초부터!",
                     listOf("JavaScript 기초", "JavaScript 기초"),
                     expandedIndex.value,
                     6,
                     6,
                     { expandedIndex.value = it }
                 )*/

            }
        }
    )
}

@Composable
fun QuizListCard(
    index: Int,
    title: String,
    description: String,
    section: List<Section>,
    expandedIndex: Int,
    completedSection: Int,
    allSection: Int,
    onClick: (Int) -> Unit
) {
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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = description,
                        color = Color.Gray
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
                                    text = "80점",
                                    modifier = Modifier.padding(6.dp)
                                )
                            }

                        }
                    } else if (completedSection != 0) {
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
                            Card(
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
                            )
                        }

                        if (completedSection == 0) {
                            Text(
                                text = "시작하기",
                                textDecoration = TextDecoration.Underline
                            )
                        } else {
                            Text(
                                text = "2025. 8. 1.",
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
        }
    )

    if (expandedIndex == index) {
        Spacer(modifier = Modifier.height(8.dp))

        section.forEach {
            val tintColor = if(it.status == true) Color.Green else Color(0xFFD8D8D8)

            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.Gray),
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
                        text = it.title,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }

    Spacer(modifier = Modifier.height(8.dp))
}


@Preview(showBackground = true)
@Composable
fun QuizScreenPreview() {
    QuizScreen()
}