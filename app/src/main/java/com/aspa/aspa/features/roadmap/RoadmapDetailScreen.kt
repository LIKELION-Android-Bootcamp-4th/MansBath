package com.aspa.aspa.features.roadmap

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.roadmap.components.SectionCard
import com.aspa.aspa.features.roadmap.navigation.RoadmapDestinations
import com.aspa.aspa.ui.theme.AspaTheme
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapDetailScreen(
    roadmapId: String,
    navController: NavController,
    viewModel: RoadmapViewModel = hiltViewModel()
) {
    val roadmapState by viewModel.roadmapState.collectAsState()

    // 최초 진입 시 로드
    LaunchedEffect(roadmapId) {
        viewModel.loadRoadmap(roadmapId)
    }

    val backStackEntry = remember {
        navController.getBackStackEntry(RoadmapDestinations.ROADMAP_DETAIL)
    }
    val savedStateHandle = backStackEntry.savedStateHandle
    LaunchedEffect(savedStateHandle) {
        savedStateHandle.getStateFlow("reload", false).collect { reload ->  // 플래그 가져오기
            if (reload) {
                delay(1000) // 임시 조치 // todo: studyViewModel에서 상태 관리한 후 해당 코드 삭제할 것
                Log.d("DETAIL", "분기 진입 완료")
                viewModel.loadRoadmap(roadmapId)
                savedStateHandle.remove<Boolean>("reload") // flag 삭제
            }
        }
    }

    when (val state = roadmapState) {
        RoadmapState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is RoadmapState.Success -> {
            val roadmap = state.roadmap
            val progress = roadmap.completedSection.toFloat() / roadmap.allSection
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0),
                topBar = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(top = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Text(
                                roadmap.title,
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color(0xFFECEEF2),
                                shape = RoundedCornerShape(6.75.dp)
                            ) {
                                Text(
                                    text = "${roadmap.completedSection}/${roadmap.allSection}",
                                    modifier = Modifier
                                        .padding(horizontal = 8.dp, vertical = 2.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            roadmap.description,
                            color = Color.Gray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text("전체 진도", fontSize = 12.sp, color = Color.Gray)
                            Text("${progress.times(100).toInt()}%", fontSize = 12.sp)
                        }

                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color.Black,
                            trackColor = Color.LightGray,
                            strokeCap = StrokeCap.Round
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Color.Black.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        )

                    }

                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                ) {
                    LazyColumn {
                        items(roadmap.sections.size) { index ->
                            SectionCard(roadmap.sections[index]) {
                                val roadmapId = roadmap.id
                                val sectionId = roadmap.sections[index].id
                                navController.navigate(
                                    RoadmapDestinations.roadmapDialog(
                                        roadmapId = roadmapId,
                                        sectionId = sectionId,
                                    )
                                )
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }


        }

        is RoadmapState.Error -> Text("❌ 에러 발생: ${state.message}")
    }
}

@Preview(showBackground = true)
@Composable
fun RoadmapDetailScreenPreview() {
    val nav = rememberNavController()
    AspaTheme {
        RoadmapDetailScreen("roadmapId", nav)
    }
}