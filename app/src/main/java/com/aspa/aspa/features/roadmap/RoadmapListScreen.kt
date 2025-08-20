package com.aspa.aspa.features.roadmap

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.data.dto.RoadmapDocumentDto
import com.aspa.aspa.data.mapper.toRoadmap
import com.aspa.aspa.features.roadmap.components.RoadmapCard
import com.aspa.aspa.features.roadmap.navigation.RoadmapDestinations
import com.aspa.aspa.model.Roadmap
import com.aspa.aspa.ui.theme.AspaTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoadmapListScreen(
    navController: NavController,
    questionId: String
) {
    val uid = Firebase.auth.uid!!

    if (questionId != "") {
        Log.d("MYTAG", "qid: $questionId")
        val roadmapId by produceState<String>(initialValue = "") {
            value = callGenerateRoadmap(uid ,questionId)
        }

        when {
            roadmapId == "" -> {
                Column (
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("로드맵 생성 중..")
                    Spacer(Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
            }
            else -> {
                Log.d("MYTAG", "roadmapId: $roadmapId")
                navController.navigate(RoadmapDestinations.roadmapDetail(roadmapId))  // todo: 이동 이후 뒤로가기 시 다시 로드맵 생성 호출됨 !!! viewModel에서 처리할 것
            }
        }

        return
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
    ) {
        val roadmaps by produceState<List<Roadmap>?>(initialValue = null, uid) {
            value = fetchRoadmaps(uid)
        }

        when {
            roadmaps == null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            roadmaps!!.isEmpty() -> {
                Text("로드맵이 존재하지 않습니다.")
            }

            else -> {
                LazyColumn {
                    items(roadmaps!!.size) { index ->

                        RoadmapCard(roadmaps!![index]) {
                            navController.navigate(RoadmapDestinations.roadmapDetail(roadmaps!![index].id))
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

suspend fun fetchRoadmaps(uid: String): List<Roadmap> {
    val db = FirebaseFirestore.getInstance()
    val snapshot = db.collection("users")
        .document(uid)
        .collection("roadmap")
        .get()
        .await()

    snapshot.documents.forEach { doc ->
        Log.d("ROADMAP_FETCH", "문서 ID: ${doc.id}, raw data = ${doc.data}")
    }

    // 문서를 DTO로 변환 후 도메인으로 매핑
    val docDtos = snapshot.toObjects(RoadmapDocumentDto::class.java)
    docDtos.forEachIndexed { index, dto ->
        Log.d("ROADMAP_FETCH", "DTO[$index].roadmap = ${dto.roadmap}")
    }

    val ids = snapshot.documents.map { it.id }
    val roadmapList = docDtos.mapIndexed { index, dto ->
        dto.roadmap.toRoadmap(ids[index]) }
    roadmapList.forEachIndexed { index, roadmap ->
        Log.d("ROADMAP_FETCH", "Roadmap[$index] = $roadmap")
    }

    return roadmapList
}


suspend fun callGenerateRoadmap (
    uid: String,
    qid: String
): String {
    Log.d("MYTAG", "uid: ${uid}")

    val result = Firebase.functions("asia-northeast3")
        .getHttpsCallable("generateRoadmap")
        .call(mapOf("uid" to uid, "questionId" to qid))
        .await()

    val payload = result.getData() as? Map<*, *>
        ?: error("Empty payload")

    val docId = payload["docId"] as? String
        ?: error("docId missing in payload")

    return docId
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