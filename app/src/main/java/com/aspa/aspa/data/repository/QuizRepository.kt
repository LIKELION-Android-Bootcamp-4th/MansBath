package com.aspa.aspa.data.repository

import com.aspa.aspa.data.dto.QuizDto
import com.aspa.aspa.data.dto.RoadmapDto
import com.aspa.aspa.data.dto.RoadmapDtoAlpha
import com.aspa.aspa.data.remote.QuizRemoteDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class QuizRepository @Inject constructor(
    private val remoteDataSource: QuizRemoteDataSource
) {
    suspend fun getRoadmapForQuiz(uid: String): Result<List<RoadmapDtoAlpha>> {
        return try {
            val roadmap = remoteDataSource.getRoadmapForQuiz(uid)
            Result.success(roadmap)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getQuiz(uid: String, quizId: String): Result<QuizDto?> {
        return try {
            val roadmap = remoteDataSource.getQuiz(uid, quizId)
            Result.success(roadmap)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendToMakeQuiz(data: String): Result<Boolean> {
        return try {
            val data = remoteDataSource.sendToMakeQuiz(data)
            Result.success(true)
        }
        catch (e: Exception) {
            Result.failure(e)
        }
    }
}