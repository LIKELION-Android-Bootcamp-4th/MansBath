package com.aspa.aspa.data.remote.service

import com.aspa.aspa.data.remote.dto.QuestionRequestDto
import com.aspa.aspa.data.remote.dto.QuestionResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface QuestionApiService {
    @POST("question")
    suspend fun sendQuestion(
        @Body request: QuestionRequestDto
    ): Response<QuestionResponseDto>
}