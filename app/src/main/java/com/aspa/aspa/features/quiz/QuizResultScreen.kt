package com.aspa.aspa.features.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.ui.components.QuizNav.QuizScreenRoute


@Composable
fun QuizResultScreen(navController: NavController) {

    val answers = listOf(
        Triple(
            "React에서 컴포넌트의 상태를 관리하기 위해 사용하는 Hook은?",
            "useState",
            "useState는 함수형 컴포넌트에서 상태를 관리하기 위한 기본적인 Hook입니다."
        ),
        Triple(
            "JSX에서 JavaScript 표현식을 사용하려면?",
            "{ }",
            "JSX에서는 중괄호 { }를 사용하여 JavaScript 표현식을 작성합니다."
        ),
        Triple(
            "useEffect Hook의 두 번째 인자로 빈 배열 []을 전달하면?",
            "컴포넌트 마운트 시에만 실행",
            "useEffect의 의존성 배열에 빈 배열을 넣는 컴포넌트가 마운트될 때만 실행됩니다."
        )
    )

    val resultDummy = QuizResultDummy.dummyQuizResult1
    var count = 0
    resultDummy.forEach {
        if(it.answer == it.chosen) count++
    }
    val correctQuestions = count
    val totalQuestions = resultDummy.size
    val score = (correctQuestions.toFloat() / totalQuestions.toFloat() * 100).toInt()


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 상단 내비게이션
        Text(
            text = "← 퀴즈 목록",
            modifier = Modifier
                .clickable {
                    navController.navigate(QuizScreenRoute.Quiz.route)
                }
                .padding(bottom = 16.dp)
        )

        // 점수 박스
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            border = BorderStroke(1.dp, Color(0xFF000000).copy(0.1f)),
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
                    tint = Color(0xFFFFCF00),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text("${score}점", style = MaterialTheme.typography.headlineMedium)
                Text("$correctQuestions/$totalQuestions 문제 정답")
            }
        }

        // 문제별 결과
        Text("문제별 결과", style = MaterialTheme.typography.titleMedium)

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(resultDummy.size) { index ->

                val questionIcon = if(resultDummy[index].chosen == resultDummy[index].answer) {
                    listOf(Icons.Default.CheckCircle, Color(0xFF4CAF50))
                } else {
                    listOf(Icons.Default.Cancel, Color.Red)
                }


                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = questionIcon[0] as ImageVector,
                                contentDescription = null,
                                tint = questionIcon[1] as Color,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("${index + 1}. ${resultDummy[index].question}", fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("당신의 답: ", fontWeight = FontWeight.SemiBold)
                            Text(
                                text = resultDummy[index].chosen,
                                color = questionIcon[1] as Color,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        if(resultDummy[index].chosen != resultDummy[index].answer) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("정답: ", fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = resultDummy[index].answer,
                                    color =  Color(0xFF4CAF50),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }


                        Spacer(modifier = Modifier.height(4.dp))

                        Card(
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFECEEF2)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.Top,
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Lightbulb,
                                    tint = Color(0xFFFFCF00),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(text = resultDummy[index].description)
                            }
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
            OutlinedButton(
                onClick = {
                    //TODO: 새로운 퀴즈 풀기 기능

                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("새로운 퀴즈 풀기")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    //TODO: 다시 풀기 기능

                    navController.navigate(QuizScreenRoute.SolveQuiz.route)
                },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("다시 풀기")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QuizResultScreenReview() {
    val navController = rememberNavController()
    QuizResultScreen(navController = navController)
}