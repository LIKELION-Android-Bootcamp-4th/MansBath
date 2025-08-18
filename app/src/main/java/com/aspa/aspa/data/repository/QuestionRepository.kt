package com.aspa.aspa.data.repository

import com.aspa.aspa.data.remote.QuestionRemoteDataSource
import com.aspa.aspa.data.remote.dto.QuestionResponseDto
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