package com.aspa.aspa.data.remote

import android.util.Log
import com.aspa.aspa.data.dto.MistakeDto
import com.aspa.aspa.data.dto.MistakeSummary
import com.aspa.aspa.data.remote.dto.QuestionResponseDto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.HttpsCallableResult
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

            val result = try {
                functions.getHttpsCallable("mistakeNotebook")
                    .call(data)
                    .await()
            } catch (e: FirebaseFunctionsException) {
                Log.e(TAG, "CF error code=${e.code}, details=${e.details}", e)
                throw e
            } catch (e: Exception) {
                Log.e(TAG, "CF call failed", e)
                throw e
            }

            val map = requireNotNull(result.getData()) { "문서를 찾을 수 없습니다" }
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
