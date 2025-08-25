package com.aspa.aspa.data.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Request DTO
 * 서버(Function)로 보낼 요청 DTO
 */
@Serializable
data class QuestionRequestDto(
    val question: String,
    val questionId: String? = null
)

/**
 * Response DTO
 * 서버(Function)로 받은 응답 DTO
 */
@Serializable
data class QuestionResponseDto(
    val questionId: String,
    @SerialName("response")
    val message: String? = null,
    val choices: List<String>? = null,
    val result: Map<String, String>? = null,
    val createdAt: String? = null
)