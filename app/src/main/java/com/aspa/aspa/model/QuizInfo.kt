package com.aspa.aspa.model

import com.aspa.aspa.data.dto.QuizzesDto
import com.google.firebase.Timestamp

data class QuizInfo(
    val title: String,
    val description: String,
    val lastModified: Timestamp,
    val quizzes: QuizzesDto
)