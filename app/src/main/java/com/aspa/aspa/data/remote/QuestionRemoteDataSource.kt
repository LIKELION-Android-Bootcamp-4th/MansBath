package com.aspa.aspa.data.remote

import com.aspa.aspa.data.remote.dto.QuestionRequestDto
import com.aspa.aspa.data.remote.dto.QuestionResponseDto
import com.aspa.aspa.data.remote.service.QuestionApiService
import com.aspa.aspa.network.RetrofitProvider

class QuestionRemoteDataSource {
    private val api: QuestionApiService = RetrofitProvider.create(QuestionApiService::class.java)

    suspend fun sendQuestion(question: String, questionId: String?): QuestionResponseDto? {
        val requestDto = QuestionRequestDto(
            question = question,
            questionId = questionId
        )

        val response = api.sendQuestion(requestDto)

        if (!response.isSuccessful) {
            throw Exception("서버 응답 실패: [${response.code()}] ${response.message()}")
        }

        return response.body()
    }
}