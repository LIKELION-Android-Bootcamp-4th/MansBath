package com.aspa2025.aspa2025.data.repository

import com.aspa2025.aspa2025.data.remote.QuestionRemoteDataSource
import com.aspa2025.aspa2025.data.remote.dto.QuestionResponseDto
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val questionRemoteDataSource: QuestionRemoteDataSource
) {
    /**
     * 질문 DTO 처리
     * 추후 onCall 방식 처리 시 기본 골자
     */
    suspend fun sendQuestion(question: String, questionId: String?): QuestionResponseDto? {
        return try {
            questionRemoteDataSource.sendQuestion(question, questionId)
        } catch (e: Exception) {
            throw e
        }
    }

}