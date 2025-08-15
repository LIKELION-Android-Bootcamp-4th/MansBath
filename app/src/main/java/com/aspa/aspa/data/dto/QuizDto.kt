package com.aspa.aspa.data.dto

import com.aspa.aspa.model.Quiz


data class RoadmapDtoAlpha(
    val roadmap: RoadmapDto = RoadmapDto()
)

data class RoadmapDto(
    val title: String = "",
    val description: String = "",
    val stages: List<StageDto> = emptyList()
)

data class StageDto(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val concept: String = "",
    val learningCurve: String = "",
    val isSolved: Boolean = false
)

data class QuizDto(
    val quizTitle: String = "",
    val questions: List<Quiz> = emptyList()
)