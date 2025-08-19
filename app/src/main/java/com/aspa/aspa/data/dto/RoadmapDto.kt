package com.aspa.aspa.data.dto

data class RoadmapDocumentDto(
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