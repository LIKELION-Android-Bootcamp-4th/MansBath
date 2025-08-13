package com.aspa.aspa.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.remote.dto.QuestionResponseDto
import com.aspa.aspa.data.repository.QuestionRepository
import com.aspa.aspa.features.home.components.UiAnalysisReport
import com.aspa.aspa.features.home.components.UiAssistantLoadingMessage
import com.aspa.aspa.features.home.components.UiAssistantMessage
import com.aspa.aspa.features.home.components.UiChatMessage
import com.aspa.aspa.features.home.components.UiUserMessage
import com.aspa.aspa.model.Auth
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

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
                    QuestionHistory(id = doc.id, title = doc.getString("title") ?: "제목 없음")
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
        questionsCollection.document(questionId).update("title", newTitle)
    }
    fun createNewChat() {
        _uiState.update {
            it.copy(messages = emptyList(), activeConversationId = null, isReportFinished = false)
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
        sendMessage(optionText, _uiState.value.activeConversationId)
    }

    fun handleFollowUpQuestion(question: String) {
        sendMessage(question, _uiState.value.activeConversationId)
    }

    private fun mapResponseDtoToUiMessage(response: QuestionResponseDto): UiChatMessage {
        return if (response.result != null) {
            UiAnalysisReport(
                id = "report_${response.questionId}",
                date = response.createdAt,
                title = "[ 사용자 분석 결과 ]",
                items = response.result
            )
        } else {
            UiAssistantMessage(
                id = "asst_${response.questionId}",
                date = response.createdAt,
                text = response.message ?: "내용이 없습니다.",
                options = response.choices
            )
        }
    }

    private fun sendMessage(text: String, questionId: String?) {
        if (!this::questionsCollection.isInitialized) {
            Log.e("HomeViewModel", "sendMessage 호출 실패: collection이 초기화되지 않았습니다.")
            return
        }

        viewModelScope.launch {
            val userMessage = UiUserMessage(id = "user_${System.currentTimeMillis()}", date = null, text = text)
            val loadingMessage = UiAssistantLoadingMessage(id = "loading")
            _uiState.update {
                it.copy(messages = it.messages + userMessage + loadingMessage)
            }

            try {
                val responseDto = questionRepository.sendQuestion(
                    question = text,
                    questionId = questionId
                )

                if (responseDto != null) {
                    val assistantMessage = mapResponseDtoToUiMessage(responseDto)
                    _uiState.update { currentState ->
                        val newMessages = currentState.messages.filterNot { it.id == "loading" } + assistantMessage
                        currentState.copy(
                            messages = newMessages,
                            activeConversationId = responseDto.questionId,
                            isReportFinished = newMessages.any { it is UiAnalysisReport }
                        )
                    }
                } else {
                    throw Exception("서버로부터 유효한 응답을 받지 못했습니다.")
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "sendMessage 실패", e)
                _uiState.update { currentState ->
                    val errorMsg = UiAssistantMessage(
                        id ="error_${System.currentTimeMillis()}",
                        date = null,
                        text = "오류가 발생했습니다: ${e.message}"
                    )
                    currentState.copy(
                        messages = currentState.messages.filterNot { it.id == "loading" } + errorMsg
                    )
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mapFirestoreHistoryToUiMessages(history: List<Map<String, Any>>, baseId: String): List<UiChatMessage> {
        return history.mapIndexedNotNull { index, item ->
            val role = item["role"] as? String ?: return@mapIndexedNotNull null
            val messageData = item["message"] ?: return@mapIndexedNotNull null
            when (role) {
                "user" -> UiUserMessage(id = "${baseId}_user_$index", date = null, text = messageData as String)
                "model" -> {
                    val modelMessageMap = messageData as Map<String, Any>
                    val message = modelMessageMap["message"] as? String ?: ""
                    val choices = modelMessageMap["choices"] as? List<String>
                    val result = modelMessageMap["result"] as? Map<String, String>
                    if (result != null) {
                        UiAnalysisReport(id = "${baseId}_report_$index", date = null, title = "[ 사용자 분석 결과 ]", items = result)
                    } else {
                        UiAssistantMessage(id = "${baseId}_asst_$index", date = null, text = message, options = choices)
                    }
                }
                else -> null
            }
        }
    }
}