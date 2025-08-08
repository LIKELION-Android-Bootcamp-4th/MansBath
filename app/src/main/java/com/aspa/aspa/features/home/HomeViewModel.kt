package com.aspa.aspa.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.repository.QuestionRepository
import com.aspa.aspa.features.home.components.UiAnalysisReport
import com.aspa.aspa.features.home.components.UiAssistantLoadingMessage
import com.aspa.aspa.features.home.components.UiAssistantMessage
import com.aspa.aspa.features.home.components.UiChatMessage
import com.aspa.aspa.features.home.components.UiUserMessage
import com.aspa.aspa.model.Auth
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
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
    private val questionRepository = QuestionRepository()
    private lateinit var questionsCollection: CollectionReference

    fun initialize() {
        val uid = Auth.uid

        if (uid == null) {
            Log.w("HomeViewModel", "UID가 null이므로 초기화할 수 없습니다.")
            return
        }

        if (this::questionsCollection.isInitialized && questionsCollection.parent?.id == uid) {
            return
        }

        questionsCollection = db.collection("users").document(uid).collection("questions")
        fetchQuestionHistories()
    }

    private fun fetchQuestionHistories() {
        if (!this::questionsCollection.isInitialized) return

        _uiState.update { it.copy(isLoading = true) }

        questionsCollection
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
        if (!this::questionsCollection.isInitialized) return
        questionsCollection.document(questionId).delete()
    }

    fun renameQuestion(questionId: String, newTitle: String) {
        if (!this::questionsCollection.isInitialized) return
        questionsCollection
            .document(questionId)
            .update("title", newTitle)
            .addOnSuccessListener {
                Log.d("Firestore", "이름 바뀜")
            }
            .addOnFailureListener { e ->
                Log.w("Firestore", "에러 발생", e)
            }
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
        if (!this::questionsCollection.isInitialized) return

        _uiState.update { it.copy(isLoading = true, messages = emptyList()) }

        questionsCollection.document(questionId)
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
        sendMessage(initialQuestion, null)
    }

    fun selectOption(optionText: String) {
        val currentId = _uiState.value.activeConversationId
        sendMessage(optionText, currentId)
    }

    fun handleFollowUpQuestion(question: String) {
        val currentId = _uiState.value.activeConversationId
        sendMessage(question, currentId)
    }

    private fun sendMessage(text: String, questionId: String?) {
        if (!this::questionsCollection.isInitialized) {
            Log.e("HomeViewModel", "sendMessage 호출 실패: collection이 초기화되지 않았습니다.")
            return
        }

        viewModelScope.launch {
            val userMessage = UiUserMessage(
                id = "user_${_uiState.value.messages.size}",
                date = null,
                text = text
            )
            val loadingMessage = UiAssistantLoadingMessage(id = "loading")

            _uiState.update {
                it.copy(messages = it.messages + userMessage + loadingMessage)
            }

            try {
                val newQuestionId = questionRepository.sendQuestion(
                    question = text,
                    questionId = questionId
                )
                if (questionId == null && newQuestionId != null) {
                    loadChatHistory(newQuestionId)
                }
            } catch (e: Exception) {
                _uiState.update { state ->
                    state.copy(messages = state.messages.filterNot { it.id == "loading" })
                }
            }
        }
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
                        val mappedResult = result.mapValues { (_, value) ->
                            value.toString()
                        }
                        UiAnalysisReport(
                            id = "${baseId}_report_$index",
                            date = null,
                            title = "[ 사용자 분석 결과 ]",
                            items = mappedResult
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