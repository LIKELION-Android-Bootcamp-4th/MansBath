package com.aspa.aspa.data.repository

import com.aspa.aspa.data.remote.RoadmapRemoteDataSource
import com.aspa.aspa.model.Roadmap
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class RoadmapRepository @Inject constructor(
    private val remote: RoadmapRemoteDataSource
) {
    suspend fun fetchRoadmaps(): Result<List<Roadmap>> =
        runCatching { remote.fetchRoadmaps() }

    suspend fun fetchRoadmap(roadmapId: String): Result<Roadmap?> =
        runCatching { remote.fetchRoadmap(roadmapId) }

    suspend fun generateRoadmap(questionId: String): Result<String> =
        runCatching { remote.generateRoadmap(questionId) }

    suspend fun isStudyExist(roadmapId: String, sectionId: Int): Result<Boolean> =
        runCatching { remote.isStudyExist(roadmapId, sectionId) }

    suspend fun isQuizExist(roadmapId: String, sectionId: Int): Result<Boolean> =
        runCatching { remote.isQuizExist(roadmapId, sectionId) }
}
