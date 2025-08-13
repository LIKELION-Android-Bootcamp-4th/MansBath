package com.aspa.aspa.features.quiz

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aspa.aspa.data.repository.QuizRepository
import com.aspa.aspa.model.Quiz
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// UI 상태를 나타내는 클래스 (로딩, 성공, 실패 등)
sealed interface QuizUiState {
    data object Loading : QuizUiState
    data class Success(val quiz: List<Quiz>) : QuizUiState
    data class Error(val message: String) : QuizUiState
}

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    // 퀴즈 목록 UI 상태를 관리하는 StateFlow
    private val _quizUiState = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val quizUiState: StateFlow<QuizUiState> = _quizUiState

    fun getQuiz(uid: String, quizId: String) {
        // ViewModel이 활성화된 동안만 동작하는 코루틴 스코프
        viewModelScope.launch {
            _quizUiState.value = QuizUiState.Loading // 로딩 상태로 변경

            repository.getQuiz(uid, quizId)
                .onSuccess { quizDto ->
                    // 성공 시 데이터 전달
                    if(quizDto != null) {
                        _quizUiState.value = QuizUiState.Success(quizDto.questions)
                    }
                    else _quizUiState.value = QuizUiState.Error("퀴즈 값이 없습니다.")
                }
                .onFailure { exception ->
                    _quizUiState.value = QuizUiState.Error(exception.message ?: "알 수 없는 오류") // 실패 시 에러 메시지 전달
                }
        }
    }

    fun getRoadmapForQuiz(uid: String) {
        viewModelScope.launch {
            // _quizUiState.value = QuizUiState.Loading // 로딩 상태로 변경

            repository.getRoadmapForQuiz(uid)
                .onSuccess { roadmap ->
                    Log.d("QuizViewModel", "데이터 불러오기 성공: $roadmap")
                }
                .onFailure { exception ->
                    Log.e("QuizViewModel", "데이터 불러오기 실패: ", exception)
                }
        }
    }



    // 퀴즈 생성을 요청하는 함수
    fun requestMakeQuiz(data: String) {
        viewModelScope.launch {
            // TODO: 퀴즈 생성 요청에 대한 UI 상태 처리 (예: 로딩 스피너 표시)

            repository.sendToMakeQuiz(data)
                .onSuccess {
                    // TODO: 성공 이벤트 처리 (예: 토스트 메시지, 화면 이동)
                }
                .onFailure { exception ->
                    // TODO: 실패 이벤트 처리 (예: 에러 다이얼로그 표시)
                }
        }
    }
}