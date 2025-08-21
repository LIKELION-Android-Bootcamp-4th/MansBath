package com.aspa.aspa.features.study

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.repository.StudyFireStoreRepository
import com.aspa.aspa.data.repository.StudyRepository
import com.aspa.aspa.features.state.UiState
import com.aspa.aspa.model.Study
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudyViewModel @Inject constructor(
    private val repository: StudyFireStoreRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val _uiState = MutableStateFlow<UiState<Study>>(UiState.Idle)
    val uiState : StateFlow<UiState<Study>> = _uiState

    private val roadmapIdFlow = savedStateHandle.getStateFlow<String?>("roadmapId", null)
    private val questionIdFlow = savedStateHandle.getStateFlow<String?>("questionId", null)
    val roadmapId = roadmapIdFlow.value
    val questionId = questionIdFlow.value

    fun fetchStudy(){
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.fetchStudy(roadmapId,questionId)
                .onSuccess {

                    _uiState.value = UiState.Success(it) }
                .onFailure {
                    android.util.Log.e("StudyVM", "fetchStudy failed", it)
                    _uiState.value = UiState.Failure(it.message ?: "Error 발생")
                }

        }
    }

}