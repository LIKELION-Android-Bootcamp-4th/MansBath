package com.aspa.aspa.features.roadmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.features.roadmap.components.RoadmapCard
import com.aspa.aspa.ui.theme.AspaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapListScreen(
    navController: NavController,
    questionId: String?
) {
    // todo: 분기 처리 필요
    // question에서 진입 시 로드맵 생성 이후 해당 디테일 로드맵으로 이동
    // 네비게이션 진입 시 그냥 전체 출력
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {

        LazyColumn {
            items(sampleRoadmaps.size) { index ->
                RoadmapCard(sampleRoadmaps[index]) {
                    navController.navigate("roadmap/detail/${sampleRoadmaps[index].title}")  // todo: title -> id
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoadmapListScreenPreview() {
    val nav = rememberNavController()

    AspaTheme {
        /**
         * wYN1b3dA0kGffMXCcv73 : 일본어를 배우고 싶어
         */
        RoadmapListScreen(questionId = "wYN1b3dA0kGffMXCcv73", navController = nav)
    }
}