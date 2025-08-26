package com.aspa.aspa.data.repository

import android.util.Log
import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.data.remote.QuizRemoteDataSource
import com.aspa.aspa.model.QuizInfo
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class QuizRepository @Inject constructor(
    private val remoteDataSource: QuizRemoteDataSource
) {
    suspend fun getQuizzes(uid: String): Result<List<QuizInfo>> {
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

    suspend fun sendToMakeQuiz(roadmapId: String, studyId: String, sectionId: Int): Result<QuizDto> {
        return try {
            val data = remoteDataSource.sendToMakeQuiz(roadmapId, studyId, sectionId)
            Log.d("QuizRepository", "퀴즈 생성 성공!")
            Result.success(data)
        }
        catch (e: Exception) {
            Log.e("QuizRepository", "퀴즈 생성 실패: ", e)
            Result.failure(e)
        }
    }

    suspend fun makeQuizFromRoadmap(uid: String, roadmapId: String, sectionId: Int): Result<QuizDto> {
        return try {
            val data = remoteDataSource.makeQuizFromRoadmap(uid, roadmapId, sectionId)
            Result.success(data)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}