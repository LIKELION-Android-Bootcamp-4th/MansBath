package com.aspa2025.aspa2025.data.remote

import android.util.Log
import com.aspa2025.aspa2025.data.dto.MistakeDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.ktx.app
import com.google.gson.Gson
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
private const val TAG = "MistakeDetailDS"
class MistakeDetailRemoteDataSource @Inject constructor(
    private val fireStore : FirebaseFirestore,
    private val gson : Gson,
    private val functions: FirebaseFunctions,
    private val firebaseAuth: FirebaseAuth
) {
    val uid = firebaseAuth.uid!!

    suspend fun getMistakeAnswerDetail(mistakeId: String): MistakeDto {
        require(mistakeId.isNotBlank()) { "mistakeId is blank" }
        val colRef = fireStore.collection("users")
            .document(uid)
            .collection("mistakeAnswer")
            .document(mistakeId)
        Log.d(TAG, "projectId=${com.google.firebase.ktx.Firebase.app.options.projectId}")
        Log.d(TAG, "uid=$uid, path=${colRef.path}, mistakeId=$mistakeId")
        val snap = colRef.get().await()
        val exists = snap.contains("response")
        if (!exists) {
            val data = mapOf(
                "uid" to uid,
                "docId" to mistakeId
            )
            functions.getHttpsCallable("mistakeNotebook")
                    .call(data)
                    .await()

            val response = colRef.get(Source.SERVER).await()
            val map = requireNotNull(response.data) { "문서를 찾을 수 없습니다" }
            val json = gson.toJson(map)
            val dto = gson.fromJson(json, MistakeDto::class.java)
            return dto


        }else{
            val map = requireNotNull(snap.data){"문서를 찾을 수 없습니다"}
            val json = gson.toJson(map)
            val dto = gson.fromJson(json,MistakeDto::class.java)
            return dto
        }
    }
    }
