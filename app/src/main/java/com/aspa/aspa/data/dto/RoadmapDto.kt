package com.aspa.aspa.data.dto

import com.google.type.Date

data class RoadmapDocumentDto(
    val roadmap: RoadmapDto = RoadmapDto()
)

data class RoadmapDto(
    val title: String = "",
    val description: String = "",
    val stages: List<StageDto> = emptyList(),
    val questionId: String = "",
    val createdAt: Date? = null,
)

data class StageDto(
    val id: Int = -1,
    val title: String = "",
    val description: String = "",
    val concept: String = "",
    val learning_curve: String = "",
    val isSolved: Boolean = false
)