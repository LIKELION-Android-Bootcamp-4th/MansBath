package com.aspa2025.aspa2025.model

import kotlinx.serialization.Serializable

@Serializable
data class Study(
    val roadmapId : String,
    val title: String,  // 로드맵의 각 섹션 타이틀
    val description: String,
    val duration: String,
    val status: Boolean,
    val items: List<StudyDetail>
)
