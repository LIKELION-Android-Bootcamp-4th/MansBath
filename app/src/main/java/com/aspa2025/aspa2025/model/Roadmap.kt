package com.aspa2025.aspa2025.model

import com.google.firebase.Timestamp

data class Roadmap (
    val id: String,
    val title: String,
    val description: String,
    var completedSection: Int,  // 2
    val allSection: Int,  // 6
    val sections: List<Section>,
    val questionId: String = "",
    val createdAt: Timestamp? = null
)