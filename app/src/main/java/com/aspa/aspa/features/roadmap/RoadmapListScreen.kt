package com.aspa.aspa.features.roadmap

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aspa.aspa.features.roadmap.components.RoadmapCard
import com.aspa.aspa.ui.theme.AspaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapListScreen(
    questionId: String?
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        LazyColumn {
            items(sampleRoadmaps.size) { index ->
                RoadmapCard(sampleRoadmaps[index])
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun RoadmapListScreenPreview() {
    AspaTheme {
        /**
         * wYN1b3dA0kGffMXCcv73 : 일본어를 배우고 싶어
         */
        RoadmapListScreen(questionId = "wYN1b3dA0kGffMXCcv73")
    }
}