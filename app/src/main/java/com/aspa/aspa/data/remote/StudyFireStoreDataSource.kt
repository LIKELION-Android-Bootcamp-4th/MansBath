package com.aspa.aspa.data.remote

import android.util.Log
import androidx.compose.animation.core.snap
import com.aspa.aspa.features.quiz.QuizState
import com.aspa.aspa.model.Study
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StudyFireStoreDataSource @Inject constructor(
    private val fireStore : FirebaseFirestore,
    private val gson : Gson,
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFunctions: FirebaseFunctions
) {
    val uid = firebaseAuth.uid!!
    suspend fun getStudyFireStore(roadmapId: String?, questionId: String?, sectionId : Int?):
            Study {
        val response = fireStore
            .collection("users")
            .document(uid)
            .collection("studies")
        try {
            val snap = response
                .whereEqualTo("roadmapId", roadmapId)
                .get()
                .await()
            if (!snap.isEmpty) {
                val document = snap.documents[0]
                val data = document.data ?: error("데이터가 비었습니다")
                val json = gson.toJson(data)
                return gson.fromJson(json, Study::class.java)
                    ?: error("파싱에 실패하였습니다.")
            } else {
                val sendData = hashMapOf(
                    "roadmapId" to roadmapId,
                    "questionId" to questionId,
                    "sectionId" to sectionId
                )
                Log.d("roadmapId",roadmapId?: "로드맵 데이터없음")
                Log.d("questionId",questionId?: "로드맵 데이터없음")
                val functions = firebaseFunctions
                    .getHttpsCallable("study")
                    .call(sendData)
                    .await()
                Log.d("Data", "생성 성공")
                val responseData = functions.getData() as? Map<*, *> 
                    ?: error("Cloud Functions 응답 데이터가 없습니다.")
                val studyDataMap = responseData["study"] as? Map<*, *>
                    ?: error("Cloud Functions 응답에 'study' 데이터가 없습니다.")

                val json = gson.toJson(studyDataMap)
                Log.d("DataSource", "추출된 study JSON: $json")

                return gson.fromJson(json, Study::class.java)
                    ?: error("생성 뒤 파싱 과정 실패하였습니다.")
            }

        } catch (e: Exception) {
            Log.e("DataSource", "데이터 검색 실패", e)
            throw e
        }

    }
}
