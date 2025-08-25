package com.aspa.aspa.data.dto

import com.google.firebase.Timestamp


data class RoadmapDocumentDto(
    val roadmap: RoadmapDto = RoadmapDto(),
    val questionId: String = "",
    val createdAt: Timestamp? = null,
)

data class RoadmapDto(
    val title: String = "",
    val description: String = "",
    val stages: List<StageDto> = emptyList(),
)

data class StageDto(
    val id: Int = -1,
    val title: String = "",
    val description: String = "",
    val concept: String = "",
    val learning_curve: String = "",
    val isSolved: Boolean = false
)