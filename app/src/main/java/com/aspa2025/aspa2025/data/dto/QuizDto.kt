package com.aspa2025.aspa2025.data.dto

import com.aspa2025.aspa2025.model.Quiz
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
    val sectionId: Int = 90, // 실제 값과 안 겹치는 dummy
    val studyId: String = "",
    val createdAt: Date? = null,
    val status: Boolean = false
)