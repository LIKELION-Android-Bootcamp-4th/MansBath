package com.aspa.aspa.data.remote

import com.aspa.aspa.data.remote.dto.QuestionResponseDto
import com.google.firebase.Firebase
import com.google.firebase.functions.functions
import com.google.firebase.functions.HttpsCallableResult
import kotlinx.coroutines.tasks.await

class QuestionRemoteDataSource {
    private val functions = Firebase.functions("asia-northeast3")

    suspend fun sendQuestion(question: String, questionId: String?): QuestionResponseDto? {
        val data = hashMapOf(
            "question" to question,
            "questionId" to questionId
        )

        val result: HttpsCallableResult = functions
            .getHttpsCallable("question")
            .call(data)
            .await()

        val resultMap = result.getData() as? Map<String, Any> ?: return null

        return QuestionResponseDto(
            questionId = resultMap["questionId"] as String,
            message = resultMap["message"] as? String,
            choices = resultMap["choices"] as? List<String>,
            result = resultMap["result"] as? Map<String, String>,
            createdAt = resultMap["createdAt"] as? String
        )
    }
}