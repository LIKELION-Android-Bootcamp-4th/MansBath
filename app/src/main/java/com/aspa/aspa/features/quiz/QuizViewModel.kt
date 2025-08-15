package com.aspa.aspa.features.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.data.dto.RoadmapDtoAlpha
import com.aspa.aspa.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface RoadmapListState {
    data object Loading: RoadmapListState
    data class Success(val roadmap: List<RoadmapDtoAlpha>): RoadmapListState
    data class Error(val error: String): RoadmapListState
}

sealed interface QuizState {
    data object Loading: QuizState
    data class Success(val quiz: QuizDto): QuizState
    data class Error(val error: String): QuizState
}

enum class SolvingState {
    NEXT,
    PREVIOUS
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _roadmapListState = MutableStateFlow<RoadmapListState>(RoadmapListState.Loading)
    val roadmapListState: StateFlow<RoadmapListState> = _roadmapListState

    private val _quizState = MutableStateFlow<QuizState>(QuizState.Loading)
    val quizState: StateFlow<QuizState> = _quizState

    private val _solvingValue = MutableStateFlow<Int>(0)
    val solvingValue: StateFlow<Int> = _solvingValue

    private val _chosenAnswerList = MutableStateFlow<List<String>>(emptyList())
    val chosenAnswerList: StateFlow<List<String>> = _chosenAnswerList

    fun getRoadMapForQuiz(uid: String) {
        viewModelScope.launch {
            _roadmapListState.value = RoadmapListState.Loading
            repository.getRoadmapForQuiz(uid)
                .onSuccess { roadmap ->
                    Log.d("QuizViewModel", "로드맵 불러오기 성공")
                    _roadmapListState.value = RoadmapListState.Success(roadmap)
                    roadmap.forEach {
                        Log.d("QuizViewModel", it.roadmap.title)
                    }

                }
                .onFailure { e ->
                    Log.e("QuizViewModel", "로드맵 불러오기 실패", e)
                    _roadmapListState.value = RoadmapListState.Error(e.message ?: "알 수 없는 오륲")
                }
        }
    }

    fun getQuiz(uid: String, quizId: String) {
        viewModelScope.launch {
            _quizState.value = QuizState.Loading
            repository.getQuiz(uid, quizId)
                .onSuccess { quiz ->
                    Log.d("QuizViewModel", "퀴즈 불러오기 성공")
                    if(quiz != null) {
                        _quizState.value = QuizState.Success(quiz)
                    }
                    else {
                        _quizState.value = QuizState.Error("요청한 퀴즈 데이터가 잘못되었습니다.")
                    }

                }
                .onFailure { e ->
                    Log.e("QuizViewModel", "퀴즈 불러오기 실패", e)
                    _quizState.value = QuizState.Error(e.message ?: "알 수 없는 오륲")
                }
        }
    }

    fun requestQuiz(quizId: String) {

        /*viewModelScope.launch {
            repository.sendToMakeQuiz(data)
                .onSuccess {

                }
                .onFailure {

                }
        }*/
    }


    fun changeSolvingValue(state: SolvingState) {
        when (state) {
            SolvingState.NEXT -> _solvingValue.update { it + 1 }
            SolvingState.PREVIOUS -> _solvingValue.update { it - 1 }
        }
    }

    fun changeSolvingChosen(index: Int, chosen: String) {
        if(_chosenAnswerList.value.size == index) {
            _chosenAnswerList.value = _chosenAnswerList.value + chosen
        }
        else {
            val newList = _chosenAnswerList.value.mapIndexed { currentIndex, currentItem ->
                if(currentIndex == index) chosen
                else currentItem
            }
            _chosenAnswerList.value = newList
        }
    }
}