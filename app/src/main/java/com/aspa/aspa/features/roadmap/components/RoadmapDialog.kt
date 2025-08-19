package com.aspa.aspa.features.roadmap.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.quiz.navigation.QuizDestinations
import com.aspa.aspa.features.roadmap.sampleRoadmap1
import com.aspa.aspa.ui.theme.AspaTheme

@Composable
fun RoadmapDialog(
    sectionId: String,
    navController: NavController,
) {
    var section = sampleRoadmap1.sections[0]  // dummy  // todo: find section using sectionId

    Dialog(onDismissRequest = {navController.popBackStack()}) {
        Surface(
            shape = RoundedCornerShape(8.75.dp),
            color = Color.White,
            tonalElevation = 8.dp,
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // 제목 & 닫기 버튼
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = section.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    Box(
                        modifier = Modifier
                            .height(24.dp)
                            .padding(top = 0.dp),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(
                            onClick = {navController.popBackStack()},
                            modifier = Modifier
                                .size(12.dp) // 충분한 터치 영역 확보
                                .padding(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "닫기",
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = section.concept,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 기간 + 상태
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.75.dp))
                            .background(Color.White, RoundedCornerShape(6.75.dp))
                            .border(
                                BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)),
                                RoundedCornerShape(6.75.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = section.duration,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFECEEF2), RoundedCornerShape(6.75.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (section.status) "완료" else "진행 예정",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Normal
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 학습 개념 (description 사용)
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Outlined.Lightbulb, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "학습 개념",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(7.dp))

                Text(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    text = section.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF717182)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 버튼
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { navController.navigate("study") },
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF030213),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(6.75.dp)
                    ) {
                        Text("학습 시작하기", style = MaterialTheme.typography.bodySmall)
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = { navController.navigate(QuizDestinations.QUIZ) {
                            popUpTo(0) { inclusive = true }
                            launchSingleTop = true
                        } },
                        modifier = Modifier
                            .weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color.Black,
                            containerColor = Color.White
                        ),
                        border = BorderStroke(width = 1.dp, color = Color.Black.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(6.75.dp)
                    ) {
                        Text("퀴즈 풀기", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true)
@Composable
fun RoadmapDialogPreview() {
    val navController = rememberNavController()
    AspaTheme {
        Scaffold {
            RoadmapDialog(
                sectionId = "",
                navController = navController
            )
        }
    }
}
