package com.aspa.aspa.data.remote

import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.data.dto.RoadmapDto
import com.aspa.aspa.data.dto.RoadmapDtoAlpha
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

    suspend fun getRoadmapForQuiz(uid: String): List<RoadmapDtoAlpha> {
        return firestore.collection("users").document(uid)
            .collection("roadmap").get().await()
            .toObjects(RoadmapDtoAlpha::class.java)
    }

    suspend fun getQuiz(uid: String, quizId: String): QuizDto? {
        return firestore.collection("users/${uid}/quizzes").document(quizId)
            .get().await()
            .toObject(QuizDto::class.java)
    }

    suspend fun sendToMakeQuiz(quizName: String) : HttpsCallableResult {
        val data = hashMapOf(
            "quizName" to quizName
        )
        return functions.getHttpsCallable("makeQuiz")
            .call(data).await()
    }

    suspend fun setQuizSolveResult(uid: String, quizId: String, chosenList: List<String>) {


    }

}