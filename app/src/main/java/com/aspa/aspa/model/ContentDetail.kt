package com.aspa.aspa.model

import kotlinx.serialization.Serializable

@Serializable
data class ContentDetail(
    val overview: String, //
    val keyPoints: List<String>,
    val details: String,
)
