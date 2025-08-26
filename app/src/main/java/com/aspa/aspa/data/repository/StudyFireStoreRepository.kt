package com.aspa.aspa.data.repository

import com.aspa.aspa.data.remote.StudyFireStoreDataSource
import javax.inject.Inject

class StudyFireStoreRepository @Inject constructor(
    private val dataSource: StudyFireStoreDataSource
){
    suspend fun fetchStudy(roadmapId: String?,questionId : String?, sectionId : Int?) = runCatching {
        dataSource.getStudyFireStore(roadmapId,questionId, sectionId)
    }
    suspend fun updateStatus(roadmapId: String?,sectionId: Int?) = runCatching { dataSource.updateStatus(roadmapId,sectionId) }

    suspend fun quizExists(roadmapId: String, sectionId: Int) = runCatching {
        dataSource.quizExists(roadmapId, sectionId)
    }
}
