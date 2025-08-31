package com.aspa2025.aspa2025.model

data class Section(
    val id: Int,
    val title: String,
    val description: String,
    val concept: String,
    val duration: String,
    val status: Boolean,
    val isSolved: Boolean,
    val quiz: Quiz,
)