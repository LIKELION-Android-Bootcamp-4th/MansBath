package com.aspa.aspa.data.repository

import com.aspa.aspa.data.remote.QuestionRemoteDataSource

class QuestionRepository {
    private val questionRemoteDataSource = QuestionRemoteDataSource()

    suspend fun sendQuestion(question: String, questionId: String?): String? {
        return try {
            val response = questionRemoteDataSource.sendQuestion(question, questionId)
            response?.questionId
        } catch (e: Exception) {
            throw e
        }
    }
}