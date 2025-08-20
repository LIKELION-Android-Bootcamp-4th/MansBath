package com.aspa.aspa.data.repository

import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.data.dto.QuizzesDto
import com.aspa.aspa.data.remote.QuizRemoteDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class QuizRepository @Inject constructor(
    private val remoteDataSource: QuizRemoteDataSource
) {
    suspend fun getQuizzes(uid: String): Result<List<QuizzesDto>> {
        return try {
            val quizzes = remoteDataSource.getQuizzes(uid)
            Result.success(quizzes)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuiz(uid: String, roadmapId: String, quizTitle: String): Result<QuizDto?> {
        return try {
            val quiz = remoteDataSource.getQuiz(uid, roadmapId, quizTitle)
            Result.success(quiz)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteQuiz(uid: String, roadmapId: String, quizTitle: String): Result<Boolean> {
        return try {
            val quiz = remoteDataSource.deleteQuiz(uid, roadmapId, quizTitle)
            Result.success(quiz)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateQuizSolveResult(uid: String, roadmapId: String, quizTitle: String, chosenList: List<String>): Result<Boolean> {
        return try {
            val isSuccess = remoteDataSource.updateQuizSolveResult(uid, roadmapId, quizTitle, chosenList)
            Result.success(isSuccess)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendToMakeQuiz(studyId: String): Result<QuizDto> {
        return try {
            val data = remoteDataSource.sendToMakeQuiz(studyId)
            Result.success(data)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}