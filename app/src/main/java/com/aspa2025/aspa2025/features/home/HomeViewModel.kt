package com.aspa2025.aspa2025.features.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa2025.aspa2025.data.repository.QuestionRepository
import com.aspa2025.aspa2025.features.home.components.UiAnalysisReport
import com.aspa2025.aspa2025.features.home.components.UiAssistantLoadingMessage
import com.aspa2025.aspa2025.features.home.components.UiAssistantMessage
import com.aspa2025.aspa2025.features.home.components.UiChatMessage
import com.aspa2025.aspa2025.features.home.components.UiUserMessage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class QuestionHistory(
    val id: String,
    val title: String,
    val hasRoadmap: Boolean
)

data class HomeUiState(
    val questionHistories: List<QuestionHistory> = emptyList(),
    val messages: List<UiChatMessage> = emptyList(),
    val isLoading: Boolean = false,
    val isReportFinished: Boolean = false,
    val roadmapId: String? = null,
    val questionId: String? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val questionRepository: QuestionRepository,
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private lateinit var questionsCollection: CollectionReference
    private val authStateListener: FirebaseAuth.AuthStateListener

    init {
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                initializeForUser(user)
            } else {
                Log.w("HomeViewModel", "사용자가 로그인되어 있지 않습니다.")
                _uiState.update { it.copy(questionHistories = emptyList(), messages = emptyList()) }
            }
        }
        auth.addAuthStateListener(authStateListener)
    }

    private fun initializeForUser(user: FirebaseUser) {
        val uid = user.uid
        val documentId = "$uid"

        Log.d("HomeViewModel", "Firestore Document ID: $documentId")

        if (this::questionsCollection.isInitialized && questionsCollection.parent?.id == documentId) {
            return
        }

        Log.d("HomeViewModel", "사용자($documentId)를 위해 초기화를 시작합니다.")
        questionsCollection = db.collection("users").document(documentId).collection("questions")
        fetchQuestionHistories()
    }

    override fun onCleared() {
        super.onCleared()
        auth.removeAuthStateListener(authStateListener)
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
                    val hasRoadmap = doc.getString("roadmapId") != null

                    QuestionHistory(
                        id = doc.id,
                        title = doc.getString("title") ?: "제목 없음",
                        hasRoadmap = hasRoadmap
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
        questionsCollection.document(questionId).update("title", newTitle)
    }
    fun createNewChat() {
        _uiState.update {
            it.copy(messages = emptyList(), questionId = null, isReportFinished = false, roadmapId = null)
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
                val roadmapId = snapshot.getString("roadmapId")

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        messages = mappedMessages,
                        questionId = questionId,
                        isReportFinished = isReportFinished,
                        roadmapId = roadmapId
                    )
                }
            }
    }

    fun startNewChat(initialQuestion: String) {
        sendMessage(initialQuestion, null)
    }

    fun selectOption(optionText: String) {
        sendMessage(optionText, _uiState.value.questionId)
    }

    fun handleFollowUpQuestion(question: String) {
        sendMessage(question, _uiState.value.questionId)
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

                if (questionId == null && responseDto?.questionId != null) {
                    loadChatHistory(responseDto.questionId)
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
            val roadmapId = item["roadmapId"] as? String

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
                        UiAssistantMessage(id = "${baseId}_asst_$index", date = null, text = message, options = choices, roadmapId = roadmapId)
                    }
                }
                else -> null
            }
        }
    }
}