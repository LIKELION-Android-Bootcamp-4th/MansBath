package com.aspa.aspa.data.dto

import com.aspa.aspa.model.Quiz
import java.util.Date

data class QuizDtoAlpha(
    val quiz: List<QuizDto> = emptyList()
)

data class QuizzesDto(
    val roadmapId: String = "",
    val quiz: List<QuizDto> = emptyList()
)

data class QuizDto(
    val quizTitle: String = "",
    val questions: List<Quiz> = emptyList(),
    val roadmapId: String = "",
    val studyId: String = "",
    val createdAt: Date? = null,
    val status: Boolean = false
)