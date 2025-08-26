package com.aspa.aspa.features.mistakeAnswer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.dto.MistakeSummary
import com.aspa.aspa.data.repository.MistakeRepository
import com.aspa.aspa.features.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MistakeAnswerViewModel @Inject constructor(
    val repository: MistakeRepository
):ViewModel() {
    private val _listState =
        MutableStateFlow<UiState<List<MistakeSummary>>>(UiState.Idle)
    val listState: StateFlow<UiState<List<MistakeSummary>>> = _listState

    fun fetchList() {
        viewModelScope.launch {
            _listState.value = UiState.Loading
            repository.fetchMistakeAnswer()
                .onSuccess { list ->
                    android.util.Log.d("MistakeVM", "fetchList success size=${list.size}")
                    _listState.value = UiState.Success(list)
                }
                .onFailure { _listState.value = UiState.Failure(it.message ?: "목록 불러오기 실패") }
        }
    }

}