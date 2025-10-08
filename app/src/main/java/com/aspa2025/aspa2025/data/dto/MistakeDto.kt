package com.aspa2025.aspa2025.data.dto

data class MistakeDto (
    val roadmapId: String = "",
    val quizTitle: String = "",
    val items: List<MistakeDetailDto> = emptyList(),
    val currentAt: String = "",
    val response: MistakeAIResponceDto = MistakeAIResponceDto()
)