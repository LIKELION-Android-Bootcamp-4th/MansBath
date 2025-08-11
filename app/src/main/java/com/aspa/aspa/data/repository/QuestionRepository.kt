package com.aspa.aspa.data.repository

import com.aspa.aspa.data.remote.QuestionRemoteDataSource
import com.aspa.aspa.data.remote.dto.QuestionResponseDto // DTO import 추가

class QuestionRepository {
    private val questionRemoteDataSource = QuestionRemoteDataSource()

    suspend fun sendQuestion(question: String, questionId: String?): QuestionResponseDto? {
        return try {
            questionRemoteDataSource.sendQuestion(question, questionId)
        } catch (e: Exception) {
            throw e
        }
    }
}