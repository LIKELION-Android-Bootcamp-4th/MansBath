package com.aspa2025.aspa2025.model

data class Quiz (
    val question: String = "",
    val options: List<String> = emptyList(),
    val answer: String = "",
    val chosen: String = "",
    val description: String = "",
)