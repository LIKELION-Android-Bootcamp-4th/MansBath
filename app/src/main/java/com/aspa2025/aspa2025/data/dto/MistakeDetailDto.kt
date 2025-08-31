package com.aspa2025.aspa2025.data.dto

data class MistakeDetailDto(
    val question: String = "",
    val answer: String = "",
    val chosen: String = "",
    val description: String = "",
    val options: List<String> = emptyList()
)
