package com.aspa.aspa.features.roadmap

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.aspa.aspa.data.dto.RoadmapDocumentDto
import com.aspa.aspa.data.dto.RoadmapDto
import com.aspa.aspa.data.mapper.toRoadmap
import com.aspa.aspa.features.roadmap.components.RoadmapCard
import com.aspa.aspa.model.Roadmap
import com.aspa.aspa.ui.theme.AspaTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

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
        val uid = "test-user-for-web"
        val roadmaps by produceState<List<Roadmap>?>(initialValue = null, uid) {
            value = fetchRoadmaps(uid) // suspend
        }

        when {
            roadmaps == null -> {
                CircularProgressIndicator()
            }

            roadmaps!!.isEmpty() -> {
                Text("로드맵이 존재하지 않습니다.")
            }

            else -> {
                LazyColumn {
                    items(roadmaps!!.size) { index ->

                        RoadmapCard(roadmaps!![index]) {
//                            navController.navigate("roadmap/detail/${sampleRoadmaps[index].title}")  // todo: title -> id
                            navController.navigate("testing.. need id")
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
    docDtos .forEachIndexed { index, dto ->
        Log.d("ROADMAP_FETCH", "DTO[$index].roadmap = ${dto.roadmap}")
    }

    val roadmapList = docDtos.map { it.roadmap.toRoadmap() }
    roadmapList.forEachIndexed { index, roadmap ->
        Log.d("ROADMAP_FETCH", "Roadmap[$index] = $roadmap")
    }

    return roadmapList
}


/*******************************
 * 로드맵 생성은 홈에서 사용자가 로드맵 생성 버튼 클릭 시 ..
 * 그렇다면 바로 detail로 진입하는 것이 자연스럽다 !!
fun callGenerateRoadmap (
    uid: String = "test-user-for-web",
    qid: String = "EZ6mff1gCh6u7Dp6vKUR"
) {
    val functions = Firebase.functions("asia-northeast3")
    val generateRoadmap = functions.getHttpsCallable("generateRoadmap")

    generateRoadmap.call(mapOf(
        "uid" to uid,
        "questionId" to qid
    ))
        .addOnSuccessListener { result ->
            val data = result.getData() as Map<*, *>
            parseResponseToRoadmap(data)
        }
        .addOnFailureListener { e ->
            Log.e("ROADMAP", "generateRoadmap 실패 ❌ : ${e.message}")
        }
}

fun parseResponseToRoadmap(data: Map<*, *>): Roadmap {
    val roadmapMap = data["data"] as Map<*, *>

}
**********************************/


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