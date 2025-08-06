package com.aspa.aspa.model

data class Section(
    val title: String,
    val description: String,
    val concept: String,
    val duration: String,
    val status: Boolean,
    val quiz: Quiz,
)