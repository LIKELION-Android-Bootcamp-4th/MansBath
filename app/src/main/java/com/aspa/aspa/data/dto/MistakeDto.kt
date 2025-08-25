package com.aspa.aspa.data.dto

data class MistakeDto (
    val roadmapId : String,
    val quizTitle : String,
    val items : List<MistakeDetailDto> = emptyList(),
)