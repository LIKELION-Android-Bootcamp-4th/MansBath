package com.aspa2025.aspa2025.features.roadmap

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspa2025.aspa2025.features.quiz.navigation.QuizDestinations
import com.aspa2025.aspa2025.ui.components.StudyNav.StudyScreenRoute
import com.aspa2025.aspa2025.ui.theme.AspaTheme

@Composable
fun RoadmapDialog(
    roadmapId: String,
    sectionId: Int,
    navController: NavController,
    viewModel: RoadmapViewModel = hiltViewModel()
) {
    val roadmapState by viewModel.roadmapState.collectAsState()
    val studyExist by viewModel.studyExistState.collectAsState()
    val quizExist by viewModel.quizExistState.collectAsState()
    val context = LocalContext.current

    var openDialog by remember { mutableStateOf(true) }
    var navigateAfterDismiss by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadRoadmap(roadmapId)
        viewModel.isStudyExist(roadmapId, sectionId)
        viewModel.isQuizExist(roadmapId, sectionId)
    }

    LaunchedEffect(openDialog) {
        if (!openDialog && !navigateAfterDismiss) {
            navController.popBackStack()
        }
    }


    when (val state = roadmapState) {
        is RoadmapState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        }

        is RoadmapState.Success -> {
            val roadmap = state.roadmap
            val section = roadmap.sections[sectionId]

            if (openDialog) {
                Dialog(onDismissRequest = { openDialog = false }) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 4.dp,
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            // 제목 & 닫기 버튼
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Computer,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = section.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                IconButton(
                                    onClick = {
                                        openDialog = false
                                        navigateAfterDismiss = false
                                    },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "닫기",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = section.concept,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            // 기간 + 상태
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    ),
                                    color = MaterialTheme.colorScheme.surface
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = section.duration,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (section.status) MaterialTheme.colorScheme.primary.copy(
                                        alpha = 0.1f
                                    ) else MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = if (section.status) "완료" else "진행 예정",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (section.status) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 4.dp
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // 학습 개념 (description 사용)
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    Icons.Outlined.Lightbulb,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.secondary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "학습 개념",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                text = section.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            // 버튼
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Button(  // 학습 버튼
                                    onClick = {
                                        when (studyExist) {
                                            true -> {
                                                openDialog = false
                                                navigateAfterDismiss = true

                                                navController.popBackStack()
                                                navController.navigate(
                                                    StudyScreenRoute.Study.study(
                                                        roadmapId,
                                                        sectionId,
                                                        roadmap.questionId
                                                    )
                                                )
                                            }

                                            false -> Toast.makeText(
                                                context,
                                                "학습 생성 중입니다.. 잠시만 기다려주세요.",
                                                Toast.LENGTH_SHORT
                                            )
                                                .show()

                                            null -> Toast.makeText(
                                                context,
                                                "학습 탐색 중..",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.primary,
                                        contentColor = MaterialTheme.colorScheme.onPrimary
                                    ),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text("학습 시작하기", style = MaterialTheme.typography.bodySmall)
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                OutlinedButton(  // 퀴즈 버튼
                                    onClick = {
                                        when (quizExist) {
                                            true -> {
                                                openDialog = false
                                                navigateAfterDismiss = true

                                                navController.navigate(QuizDestinations.QUIZ_GRAPH_ROUTE) {
                                                    popUpTo(0) { inclusive = true }
                                                    launchSingleTop = true
                                                }
                                            }

                                            false -> Toast.makeText(
                                                context,
                                                "퀴즈가 존재하지 않습니다.\n먼저 학습을 시작해주세요.",
                                                Toast.LENGTH_SHORT
                                            ).show()

                                            null -> Toast.makeText(
                                                context,
                                                "퀴즈 탐색 중..",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = MaterialTheme.colorScheme.primary
                                    ),
                                    border = BorderStroke(
                                        1.dp,
                                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                    ),
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Text("퀴즈 풀기", style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }

            // 시스템 뒤로가기 시
            BackHandler(enabled = openDialog) {
                openDialog = false
                navigateAfterDismiss = false
            }
        }

        is RoadmapState.Error -> Text(
            "❌ 에러 발생: ${state.message}",
            color = MaterialTheme.colorScheme.error
        )
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
                roadmapId = "",
                sectionId = -1,
                navController = navController
            )
        }
    }
}
