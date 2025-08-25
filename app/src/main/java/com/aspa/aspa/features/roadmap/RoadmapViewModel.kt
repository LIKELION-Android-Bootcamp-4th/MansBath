package com.aspa.aspa.features.roadmap

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.repository.RoadmapRepository
import com.aspa.aspa.model.Roadmap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RoadmapListState {
    object Loading : RoadmapListState
    object Empty : RoadmapListState
    data class Success(val roadmaps: List<Roadmap>) : RoadmapListState
    data class Error(val message: String) : RoadmapListState
}

sealed interface RoadmapState {
    object Loading : RoadmapState
    data class Success(val roadmap: Roadmap) : RoadmapState
    data class Error(val message: String) : RoadmapState
}

@HiltViewModel
class RoadmapViewModel @Inject constructor(
    private val repository: RoadmapRepository
) : ViewModel() {

    private val _roadmapListState = MutableStateFlow<RoadmapListState>(RoadmapListState.Loading)
    val roadmapListState: StateFlow<RoadmapListState> = _roadmapListState

    private val _roadmapState = MutableStateFlow<RoadmapState>(RoadmapState.Loading)
    val roadmapState: StateFlow<RoadmapState> = _roadmapState

    private val _generateState = MutableStateFlow<Boolean?>(null)
    val generateState: StateFlow<Boolean?> = _generateState

    private val _quizExistState = MutableStateFlow<Boolean?>(null)
    val quizExistState: StateFlow<Boolean?> = _quizExistState

    fun loadRoadmaps() {
        viewModelScope.launch {
            _roadmapListState.value = RoadmapListState.Loading
            repository.fetchRoadmaps()
                .onSuccess { roadmapList ->
                    _roadmapListState.value =
                        if (roadmapList.isEmpty()) RoadmapListState.Empty
                        else RoadmapListState.Success(roadmapList)
                }
                .onFailure { _roadmapListState.value = RoadmapListState.Error(it.message ?: "❌ 로드맵 목록 조회 실패") }
        }
    }

    fun loadRoadmap(roadmapId: String) {
        viewModelScope.launch {
            _roadmapState.value = RoadmapState.Loading
            repository.fetchRoadmap(roadmapId)
                .onSuccess { roadmap ->
                    if (roadmap != null)
                        _roadmapState.value = RoadmapState.Success(roadmap)
                    else
                        _roadmapState.value = RoadmapState.Error("❌ 로드맵 없음")
                }
                .onFailure { _roadmapState.value = RoadmapState.Error(it.message ?: "❌ 로드맵 상세 조회 실패") }
        }
    }

    fun generateRoadmap(questionId: String, onGenerated: (String) -> Unit) {
        viewModelScope.launch {
            repository.generateRoadmap(questionId)
                .onSuccess { roadmapId ->
                    _generateState.value = true
                    onGenerated(roadmapId)
                }
                .onFailure { e ->
                    _generateState.value = false
                    e.printStackTrace()
                    Log.e("RoadmapViewModel", "❌ 로드맵 생성 실패", e)
                }
        }
    }

    fun isQuizExist(roadmapId: String) {
        viewModelScope.launch {
            repository.isQuizExist(roadmapId)
                .onSuccess { exists ->
                    _quizExistState.value = exists
                }
                .onFailure { e ->
                    e.printStackTrace()
                    _quizExistState.value = false
                }
        }
    }
}
