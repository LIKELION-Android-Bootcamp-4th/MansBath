package com.aspa2025.aspa2025.model

import kotlinx.serialization.Serializable

@Serializable
data class ContentDetail(
    val overview: String, //
    val keyPoints: List<String>,
    val details: String,
)
