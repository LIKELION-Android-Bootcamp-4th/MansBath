package com.aspa.aspa.data.dto

data class MistakeAnswerDto(
    val question : String = "",
    val answer : String = "",
    val chosen : String = "",
    val explanation : String = "",
    val options : List<String> = emptyList(),
)
