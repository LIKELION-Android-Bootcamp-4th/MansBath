package com.aspa.aspa.features.roadmap

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.roadmap.components.RoadmapCard
import com.aspa.aspa.features.roadmap.navigation.RoadmapDestinations
import com.aspa.aspa.features.widget.RoadmapStatusWidgetProvider
import com.aspa.aspa.ui.theme.AspaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapListScreen(
    navController: NavController,
    questionId: String,
    fromWidget: Boolean,
    viewModel: RoadmapViewModel = hiltViewModel()
) {
    Log.d("MYTAG", "qid: $questionId")

    val roadmapListState by viewModel.roadmapListState.collectAsState()
    val generateState by viewModel.generateState.collectAsState()

    LaunchedEffect(Unit) {
        if (questionId.isNotEmpty()) {
            viewModel.generateRoadmap(questionId) { roadmapId ->
                // 먼저 questionId 없는 목록으로 교체
                navController.navigate(RoadmapDestinations.roadmapList()) {
                    popUpTo(0)
                }
                // 그 다음에 상세 화면으로 이동
                navController.navigate(
                    RoadmapDestinations.roadmapDetail(
                        roadmapId
                    )
                )
            }
        } else {
            viewModel.loadRoadmaps()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        if (questionId.isNotEmpty()) {
            GenerateRoadmapSection(generateState)
        } else {
            RoadmapListSection(roadmapListState, fromWidget, navController)
        }
    }
}

@Composable
private fun GenerateRoadmapSection(generateState: Boolean?) {
    when (generateState) {
        null -> LoadingContent("로드맵 생성 중..")
        true -> Unit // 성공 시 바로 Detail로 이동되므로 UI 없음
        false -> Text(
            "❌ 로드맵 생성 실패",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun RoadmapListSection(
    state: RoadmapListState,
    fromWidget: Boolean,
    navController: NavController,
) {
    val context = LocalContext.current

    when (state) {
        is RoadmapListState.Loading -> LoadingContent("로드맵 조회 중..")
        is RoadmapListState.Empty -> Text(
            "로드맵이 존재하지 않습니다.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        is RoadmapListState.Success -> {
            LazyColumn {
                items(state.roadmaps.size) { index ->
                    val roadmap = state.roadmaps[index]
                    RoadmapCard(roadmap) {
                        if (fromWidget) {
                            val intent = Intent(context, RoadmapStatusWidgetProvider::class.java).apply {
                                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                                putExtra("ROADMAP_TITLE", roadmap.title)
                                putExtra("ROADMAP_COMPLETED", roadmap.completedSection)
                                putExtra("ROADMAP_ALL", roadmap.allSection)
                            }

                            context.sendBroadcast(intent)

                            if (context is Activity) {
                                context.finish()
                            }
                        }

                        navController.navigate(
                            RoadmapDestinations.roadmapDetail(
                                roadmap.id
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
        is RoadmapListState.Error -> Text(
            "❌ 에러 발생: ${state.message}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun LoadingContent(message: String) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(16.dp))
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

/*
@Preview(showBackground = true)
@Composable
fun RoadmapListScreenPreview() {
    val nav = rememberNavController()

    AspaTheme {
        /**
         * wYN1b3dA0kGffMXCcv73 : 일본어를 배우고 싶어
         */
        RoadmapListScreen(
            questionId = "wYN1b3dA0kGffMXCcv73",
            navController = nav,
            fromWidget = false,
        )
    }
}*/
