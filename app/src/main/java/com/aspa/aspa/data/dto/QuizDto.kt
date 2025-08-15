package com.aspa.aspa.data.dto

import com.aspa.aspa.model.Quiz

data class QuizDto (
    val quizTitle: String,
    val questions: List<Quiz>
)