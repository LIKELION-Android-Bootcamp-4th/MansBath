package com.aspa.aspa.data.remote

import android.util.Log
import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.model.Quiz
import com.aspa.aspa.model.Roadmap
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.HttpsCallableResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class QuizRemoteDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val functions: FirebaseFunctions
) {
    suspend fun getQuiz(uid: String, quizId: String): QuizDto? {
        return firestore.collection("users/${uid}/quizzes")
            .document(quizId).get().await()
            .toObject(QuizDto::class.java) // QuizDto 모델로 변환
    }

    suspend fun getRoadmapForQuiz(uid: String): List<Roadmap> {
        return firestore.collection("users").document(uid)
            .collection("roadmap").get().await()
            .toObjects(Roadmap::class.java)
    }

    suspend fun sendToMakeQuiz(quizName: String): HttpsCallableResult {
        val data = hashMapOf(
            "quizName" to quizName
        )

        return functions
            .getHttpsCallable("makeQuiz")
            .call(data)
            .await()

        /*functions.getHttpsCallable("makeQuiz")
            .call(data)
            .addOnSuccessListener { result: HttpsCallableResult ->
                Log.d("makeQuiz", "성공: ${result.getData()}")
            }
            .addOnFailureListener { e ->
                Log.e("makeQuiz", "오류 발생", e)
            }*/
    }

}