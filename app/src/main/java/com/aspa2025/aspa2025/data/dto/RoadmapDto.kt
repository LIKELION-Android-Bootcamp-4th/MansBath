package com.aspa2025.aspa2025.data.dto

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
    val status: Boolean = false,
    val isSolved: Boolean = false
)