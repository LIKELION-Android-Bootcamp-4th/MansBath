package com.aspa.aspa.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.features.home.components.UiAnalysisReport
import com.aspa.aspa.features.home.components.UiAssistantMessage
import com.aspa.aspa.features.home.components.UiChatMessage
import com.aspa.aspa.features.home.components.UiUserMessage
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class QuestionHistory(
    val id: String,
    val title: String
)

data class HomeUiState(
    val questionHistories: List<QuestionHistory> = emptyList(),
    val messages: List<UiChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val isReportFinished: Boolean = false,
    val activeConversationId: String? = null
)

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val db = Firebase.firestore
    private val userId = "test-user-for-web"

    init {
        fetchQuestionHistories()
    }

    private fun fetchQuestionHistories() {
        _uiState.update { it.copy(isLoading = true) }

        db.collection("users").document(userId).collection("questions")
            .orderBy("lastUpdatedAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshots, error ->
                if (error != null) {
                    Log.w("Firestore", "Listen failed.", error)
                    _uiState.update { it.copy(isLoading = false) }
                    return@addSnapshotListener
                }

                val histories = snapshots?.map { doc ->
                    QuestionHistory(
                        id = doc.id,
                        title = doc.getString("title") ?: "제목 없음"
                    )
                } ?: emptyList()

                _uiState.update { it.copy(isLoading = false, questionHistories = histories) }
            }
    }

    fun deleteQuestionHistory(questionId: String) {
        db.collection("users").document(userId).collection("questions")
            .document(questionId)
            .delete()
    }

    fun createNewChat() {
        _uiState.update {
            it.copy(
                messages = emptyList(),
                activeConversationId = null,
                isReportFinished = false
            )
        }
    }

    fun loadChatHistory(questionId: String) {
        _uiState.update { it.copy(isLoading = true, messages = emptyList()) }

        db.collection("users").document(userId).collection("questions").document(questionId)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null || !snapshot.exists()) {
                    Log.w("Firestore", "Load chat history failed for $questionId", error)
                    _uiState.update { it.copy(isLoading = false) }
                    return@addSnapshotListener
                }

                val history = snapshot.get("history") as? List<Map<String, Any>> ?: emptyList()
                val mappedMessages = mapFirestoreHistoryToUiMessages(history, questionId)
                val isReportFinished = mappedMessages.any { it is UiAnalysisReport }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        messages = mappedMessages,
                        activeConversationId = questionId,
                        isReportFinished = isReportFinished
                    )
                }
            }
    }

    fun startNewChat(initialQuestion: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, messages = emptyList()) }

            val userMessage = UiUserMessage("user_0", null, initialQuestion)
            _uiState.update { it.copy(messages = listOf(userMessage)) }

            val newQuestion = mapOf(
                "title" to initialQuestion,
                "history" to listOf(mapOf("role" to "user", "message" to initialQuestion)),
                "createdAt" to FieldValue.serverTimestamp(),
                "lastUpdatedAt" to FieldValue.serverTimestamp()
            )

            db.collection("users").document(userId).collection("questions")
                .add(newQuestion)
                .addOnSuccessListener { documentReference ->
                    loadChatHistory(documentReference.id)
                }
                .addOnFailureListener {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun selectOption(optionText: String) {
        val currentId = _uiState.value.activeConversationId ?: return

        val userHistory = mapOf(
            "role" to "user",
            "message" to optionText,
            "createdAt" to FieldValue.serverTimestamp()
        )

        db.collection("users").document(userId)
            .collection("questions").document(currentId)
            .update("history", FieldValue.arrayUnion(userHistory))
    }

    fun handleFollowUpQuestion(question: String) {
        val currentId = _uiState.value.activeConversationId ?: return

        val userHistory = mapOf(
            "role" to "user",
            "message" to question,
            "createdAt" to FieldValue.serverTimestamp()
        )

        db.collection("users").document(userId)
            .collection("questions").document(currentId)
            .update("history", FieldValue.arrayUnion(userHistory))
    }

    private fun mapFirestoreHistoryToUiMessages(history: List<Map<String, Any>>, baseId: String): List<UiChatMessage> {
        return history.mapIndexedNotNull { index, item ->
            val role = item["role"] as? String ?: return@mapIndexedNotNull null
            val messageData = item["message"] ?: return@mapIndexedNotNull null

            when (role) {
                "user" -> UiUserMessage(
                    id = "${baseId}_user_$index",
                    date = null,
                    text = messageData as String
                )
                "model" -> {
                    val modelMessageMap = messageData as Map<String, Any>
                    val message = modelMessageMap["message"] as? String ?: ""
                    val choices = modelMessageMap["choices"] as? List<String>
                    val result = modelMessageMap["result"] as? Map<String, String>

                    if (result != null) {
                        UiAnalysisReport(
                            id = "${baseId}_report_$index",
                            date = null,
                            title = "[사용자 분석 결과]",
                            items = result
                        )
                    } else {
                        UiAssistantMessage(
                            id = "${baseId}_asst_$index",
                            date = null,
                            text = message,
                            options = choices
                        )
                    }
                }
                else -> null
            }
        }
    }
}