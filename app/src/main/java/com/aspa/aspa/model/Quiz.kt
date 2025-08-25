package com.aspa.aspa.model

data class Quiz (
    val question: String = "",
    val options: List<String> = emptyList(),
    val answer: String = "",
    val chosen: String = "",
    val description: String = "",
)