package com.aspa.aspa.features.home.components

import java.util.Date

data class ModelMessage(
    val message: String = "",
    val choices: List<String>? = null,
    val result: Map<String, String>? = null
)

sealed interface UiChatMessage {
    val id: String
    val date: Date?
}

data class UiUserMessage(
    override val id: String,
    override val date: Date?,
    val text: String
) : UiChatMessage

data class UiAssistantMessage(
    override val id: String,
    override val date: Date?,
    val text: String,
    val options: List<String>? = null
) : UiChatMessage

data class UiAnalysisReport(
    override val id: String,
    override val date: Date?,
    val title: String,
    val items: Map<String, String>
) : UiChatMessage