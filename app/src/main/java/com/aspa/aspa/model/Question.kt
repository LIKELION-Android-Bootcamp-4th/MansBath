package com.aspa.aspa.features.home.components

sealed interface UiChatMessage {
    val id: String
    val date: String?
}

data class UiUserMessage(
    override val id: String,
    override val date: String?,
    val text: String
) : UiChatMessage

data class UiAssistantMessage(
    override val id: String,
    override val date: String?,
    val text: String,
    val options: List<String>? = null
) : UiChatMessage

data class UiAnalysisReport(
    override val id: String,
    override val date: String?,
    val title: String,
    val items: Map<String, String>
) : UiChatMessage

data class UiAssistantLoadingMessage(
    override val id: String = "loading",
    override val date: String? = null
) : UiChatMessage