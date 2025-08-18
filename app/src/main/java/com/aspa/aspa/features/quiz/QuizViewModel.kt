package com.aspa.aspa.features.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.data.dto.QuizDtoAlpha
import com.aspa.aspa.data.dto.QuizzesDto
import com.aspa.aspa.data.dto.RoadmapDtoAlpha
import com.aspa.aspa.data.repository.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface QuizListState {
    data object Loading : QuizListState
    data class Success(val quizzes: List<QuizzesDto>) : QuizListState
    data class Error(val error: String) : QuizListState
}

sealed interface QuizState {
    data object Loading : QuizState
    data class Success(val quiz: QuizDto) : QuizState
    data class Error(val error: String) : QuizState
}

enum class SolvingState {
    NEXT,
    PREVIOUS
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _quizListState = MutableStateFlow<QuizListState>(QuizListState.Loading)
    val quizListState: StateFlow<QuizListState> = _quizListState

    private val _quizState = MutableStateFlow<QuizState>(QuizState.Loading)
    val quizState: StateFlow<QuizState> = _quizState

    private val _solvingValue = MutableStateFlow<Int>(0)
    val solvingValue: StateFlow<Int> = _solvingValue

    private val _chosenAnswerList = MutableStateFlow(List(10) { "" })
    val chosenAnswerList: StateFlow<List<String>> = _chosenAnswerList

    fun getQuizzes(uid: String) {
        viewModelScope.launch {
            _quizListState.value = QuizListState.Loading
            repository.getQuizzes(uid)
                .onSuccess { quizzes ->
                    Log.d("QuizViewModel", "퀴즈 리스트 불러오기 성공")
                    _quizListState.value = QuizListState.Success(quizzes)
                    quizzes.forEach {
                        Log.d("QuizViewModel", it.toString())
                        it.quiz.forEach {
                            Log.d("QuizViewModel", it.quizTitle)
                        }
                    }

                }
                .onFailure { e ->
                    Log.e("QuizViewModel", "퀴즈 리스트 불러오기 실패", e)
                    _quizListState.value = QuizListState.Error(e.message ?: "알 수 없는 오륲")
                }
        }
    }

    fun getQuiz(uid: String, roadmapId: String, quizTitle: String) {
        viewModelScope.launch {
            _quizState.value = QuizState.Loading
            repository.getQuiz(uid, roadmapId, quizTitle)
                .onSuccess { quiz ->
                    Log.d("QuizViewModel", "퀴즈 불러오기 성공")
                    if (quiz != null) {
                        _quizState.value = QuizState.Success(quiz)
                    } else {
                        _quizState.value = QuizState.Error("요청한 퀴즈 데이터가 잘못되었습니다.")
                    }

                }
                .onFailure { e ->
                    Log.e("QuizViewModel", "퀴즈 불러오기 실패", e)
                    _quizState.value = QuizState.Error(e.message ?: "알 수 없는 오륲")
                }
        }
    }

    fun deleteQuiz(uid: String, roadmapId: String, quizTitle: String) {
        viewModelScope.launch {
            repository.deleteQuiz(uid, roadmapId, quizTitle)
                .onSuccess { success ->
                    if (success) {
                        Log.d("QuizViewModel", "퀴즈 삭제 성공")
                    } else {
                        Log.e("QuizViewModel", "퀴즈 삭제 중 문제 발생..")
                    }

                }
                .onFailure { e ->
                    Log.e("QuizViewModel", "퀴즈 삭제 실패", e)
                }
        }
    }

    fun requestQuiz(studyId: String) {
        viewModelScope.launch {
            _quizState.value = QuizState.Loading
            repository.sendToMakeQuiz(studyId)
                .onSuccess { quiz ->
                    Log.d("QuizViewModel", "퀴즈 생성 성공")
                    if (quiz.quizTitle != "") {
                        _quizState.value = QuizState.Success(quiz)
                    } else {
                        _quizState.value = QuizState.Error("요청한 스터디 데이터가 잘못되었습니다.")
                    }
                }
                .onFailure { e ->
                    Log.e("QuizViewModel", "퀴즈 생성 실패", e)
                    _quizState.value = QuizState.Error(e.message ?: "알 수 없는 오륲")
                }
        }
    }


    fun changeSolvingValue(state: SolvingState) {
        when (state) {
            SolvingState.NEXT -> _solvingValue.update { it + 1 }
            SolvingState.PREVIOUS -> _solvingValue.update { it - 1 }
        }
    }

    fun changeSolvingChosen(index: Int, chosen: String) {
        _chosenAnswerList.value = _chosenAnswerList.value.toMutableList().also {
            if (index in it.indices) {
                it[index] = chosen
            } else {
                throw IndexOutOfBoundsException("Index $index is out of bounds for chosenAnswerList")
            }
        }
        /*val newList = _chosenAnswerList.value.mapIndexed { currentIndex, currentItem ->
            if (currentIndex == index) chosen
            else currentItem
        }
        _chosenAnswerList.value = newList*/
    }

    fun saveSolvedChosen(uid: String, roadmapId: String, quizTitle: String, chosenList: List<String>) {
        viewModelScope.launch {
            repository.updateQuizSolveResult(uid, roadmapId, quizTitle, chosenList)
                .onSuccess {
                    Log.d("QuizViewModel", "퀴즈 풀이 데이터 저장 성공")
                }
                .onFailure { e ->
                    Log.e("QuizViewModel", "퀴즈 풀이 데이터 저장 실패", e)
                }
        }
    }

    fun solveQuizAgain() {
        _solvingValue.value = 0
        _chosenAnswerList.value = List(10) { "" }

    }

    fun syncChosenToQuestions() {
        val currentState = _quizState.value
        if (currentState is QuizState.Success) {
            val updatedQuestions = currentState.quiz.questions.mapIndexed { i, q ->
                q.copy(chosen = _chosenAnswerList.value[i])
            }
            _quizState.value = currentState.copy(
                quiz = currentState.quiz.copy(questions = updatedQuestions)
            )
        }
    }
}