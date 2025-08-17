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
    val id: Long? = null,
    val title: String = "",
    val description: String = "",
    val learning_curve: String = "",
    val concept: String = ""
)