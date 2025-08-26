package com.aspa.aspa.features.mistakeAnswer.MistakeAnswerDetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.dto.MistakeDto
import com.aspa.aspa.data.dto.MistakeSummary
import com.aspa.aspa.data.repository.MistakeDetailRepository
import com.aspa.aspa.features.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MistakeAnswerDetailViewModel @Inject constructor(
    private val repository: MistakeDetailRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel(){
    private val _listState =
        MutableStateFlow<UiState<MistakeDto>>(UiState.Idle)
    val listState: StateFlow<UiState<MistakeDto>> = _listState
    private val mistakeIdFlow = savedStateHandle.getStateFlow("mistakeId", "")
    val mistakeId = mistakeIdFlow.value

    fun fetchList() {
        viewModelScope.launch {
            _listState.value = UiState.Loading
            repository.fetchMistakeDetail(mistakeId)
                .onSuccess {dto ->
                    _listState.value = UiState.Success(dto)
                }
                .onFailure { _listState.value = UiState.Failure(it.message ?: "목록 불러오기 실패") }
        }
    }
}