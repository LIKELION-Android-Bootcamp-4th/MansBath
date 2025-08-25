package com.aspa.aspa.data.remote

import androidx.compose.animation.core.snap
import com.aspa.aspa.model.Study
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StudyFireStoreDataSource @Inject constructor(
    private val fireStore : FirebaseFirestore,
    private val gson : Gson
){
    val testId = "test-user-for-web" //나중에 값 들어오는거 바꿔주기
    val testFieldId = "3주 초단기 JLPT N3 합격 로드맵"
    suspend fun getStudyFireStore(fieldId : String? = null,id : String ? = null) :
            Study {
        val response = fireStore
            .collection("users")
            .document(testId)
            .collection("studies")
            .document(testFieldId)
            .get()
            .await()

        val map = response.data ?: error(" 데이터가 비어있습니다.")
        val json = gson.toJson(map)
        return gson.fromJson(json, Study::class.java)
            ?: error("파싱에 실패하였습니다")
    }
}