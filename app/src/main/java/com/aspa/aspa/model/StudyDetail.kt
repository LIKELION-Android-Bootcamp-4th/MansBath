package com.aspa.aspa.model

data class StudyDetail (
    val title : String,
    val subtitle : List<String>,  // 이러면 어쨋든 하위 목차 3개는 뽑아달라고 system message 필요
    val content: List<String>,
    // 개요: String
    // 포인트: String
    // 상세 내용: String
    // 예제: String  // <<- 따로 UI 작업 좀 해라~
)