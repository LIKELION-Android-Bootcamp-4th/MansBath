package com.aspa.aspa.features.quiz

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun SolveQuizScreen() {
    var selectedOption by remember { mutableStateOf("useState") }

    Scaffold(
        content = { padding->
            Column (
                modifier = Modifier.padding(padding).padding(8.dp)
            ) {
                Text(
                    text = "← 나가기",
                    modifier = Modifier
                        .clickable {  }
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "React 기초 퀴즈",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                    )

                    Text(
                        text = "1/3",
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = 1f / 3f,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    border = BorderStroke(1.dp, Color.Gray),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("React에서 컴포넌트의 상태를 관리하기 위해 사용하는 Hook은?")

                        Spacer(modifier = Modifier.height(16.dp))

                        val options = listOf("useEffect", "useState", "useContext", "useReducer")
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
                                            containerColor = Color(0xF0406DFD)
                                        )
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

                                /*if(selectedOption == option) {
                                    Card(
                                        modifier = Modifier.fillMaxSize(),
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xF0406DFD)
                                        ),
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = option,
                                                modifier = Modifier
                                                    .padding(horizontal = 6.dp)
                                            )

                                        }
                                    }
                                }
                                else {
                                    Text(text = option, modifier = Modifier.padding(horizontal = 4.dp))
                                }*/

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
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("이전")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {},
                        enabled = selectedOption.isNotEmpty(),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xF0406DFD)
                        )
                    ) {
                        Text("다음")
                    }
                }

            }

        }
    )
}

@Preview
@Composable
fun SolveQuizScreenPreview() {
    SolveQuizScreen()
}