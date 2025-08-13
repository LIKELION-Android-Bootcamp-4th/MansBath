package com.aspa.aspa.data.repository

import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.data.remote.QuizRemoteDataSource
import com.aspa.aspa.model.Quiz
import com.aspa.aspa.model.Roadmap
import dagger.hilt.android.scopes.ViewModelScoped
import jakarta.inject.Inject
import jakarta.inject.Singleton

@ViewModelScoped // 앱 전역에서 하나의 인스턴스만 사용하도록 설정
class QuizRepository @Inject constructor(
    private val remoteDataSource: QuizRemoteDataSource
) {

    /**
     * 원격 DataSource를 호출하여 퀴즈를 가져옵니다.
     * Result 클래스로 래핑하여 성공/실패 케이스를 명확하게 처리합니다.
     */
    suspend fun getQuiz(uid: String, quizId: String): Result<QuizDto?> {
        return try {
            val quiz = remoteDataSource.getQuiz(uid, quizId)
            Result.success(quiz)
        } catch (e: Exception) {
            // Firestore 통신 중 발생한 예외 처리
            Result.failure(e)
        }
    }

    /**
     * 원격 DataSource를 호출하여 로드맵 목록을 가져옵니다.
     * Result 클래스로 래핑하여 성공/실패 케이스를 명확하게 처리합니다.
     */
    suspend fun getRoadmapForQuiz(uid: String): Result<List<Roadmap>> {
        return try {
            val roadmap = remoteDataSource.getRoadmapForQuiz(uid)
            Result.success(roadmap)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * 원격 DataSource를 호출하여 퀴즈 생성 요청을 보냅니다.
     */
    suspend fun sendToMakeQuiz(data: String): Result<Boolean> {
        return try {
            remoteDataSource.sendToMakeQuiz(data)
            Result.success(true)
        } catch (e: Exception) {
            // Cloud Function 호출 중 발생한 예외 처리
            Result.failure(e)
        }
    }
}