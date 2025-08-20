package com.aspa.aspa.model

data class Roadmap (
    val id: String,
    val title: String,
    val description: String,
    val completedSection: Int,  // 2
    val allSection: Int,  // 6
    val sections: List<Section>,
)