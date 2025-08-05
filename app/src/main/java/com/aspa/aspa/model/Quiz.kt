package com.aspa.aspa.model

data class Quiz (
    val question: String,
    val choices: List<String>,
    val answer: String,
    val chosen: String = "",
    val description: String,
)