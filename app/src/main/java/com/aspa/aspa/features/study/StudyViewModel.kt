package com.aspa.aspa.features.study

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.repository.QuizRepository
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspa.aspa.data.repository.StudyFireStoreRepository
import com.aspa.aspa.data.repository.StudyRepository
import com.aspa.aspa.features.state.MakeQuizState
import com.aspa.aspa.features.state.UiState
import com.aspa.aspa.model.Study
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val repository: StudyFireStoreRepository,
    private val savedStateHandle: SavedStateHandle,
    private val quizRepository: QuizRepository,
    private val auth: FirebaseAuth
): ViewModel() {
    val _uiState = MutableStateFlow<UiState<Study>>(UiState.Idle)
    val uiState : StateFlow<UiState<Study>> = _uiState

    private val roadmapIdFlow = savedStateHandle.getStateFlow("roadmapId", "")
    private val questionIdFlow = savedStateHandle.getStateFlow<String?>("questionId", null)
    private val sectionIdFlow = savedStateHandle.getStateFlow("sectionId", -1)
    val roadmapId: String get() = roadmapIdFlow.value
    val questionId = questionIdFlow.value
    val sectionId = sectionIdFlow.value

    private val _makeQuizFlow = MutableSharedFlow<MakeQuizState>()
    val makeQuizFlow = _makeQuizFlow.asSharedFlow()

    fun fetchStudy(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.fetchStudy(roadmapId,questionId,sectionId)
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }
                .onFailure {
                    android.util.Log.e("StudyVM", "fetchStudy failed", it)
                    _uiState.value = UiState.Failure(it.message ?: "Error 발생")
                }

        }
    }
    fun updateStatus(){
        viewModelScope.launch {
            repository.updateStatus(roadmapId,sectionId)
        }
    }

    // TODO: 트랜잭션 처리시 제거..
    fun makeQuiz() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            quizRepository.makeQuizFromRoadmap(auth.currentUser!!.uid, roadmapId, sectionId)
                .onSuccess {
                    _makeQuizFlow.emit(MakeQuizState.Navigate)
                }
                .onFailure {
                    android.util.Log.e("StudyVM", "makeQuiz failed", it)
                    _uiState.value = UiState.Failure(it.message ?: "퀴즈 생성 중 에러 발생")
                }
        }
    }

}