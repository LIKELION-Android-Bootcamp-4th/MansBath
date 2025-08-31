package com.aspa2025.aspa2025.model

import com.aspa2025.aspa2025.data.dto.QuizzesDto
import com.google.firebase.Timestamp

data class QuizInfo(
    val title: String,
    val description: String,
    val lastModified: Timestamp,
    val quizzes: QuizzesDto
)