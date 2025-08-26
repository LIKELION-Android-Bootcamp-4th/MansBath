package com.aspa.aspa.data.repository

import com.aspa.aspa.data.remote.StudyFireStoreDataSource
import javax.inject.Inject

class StudyFireStoreRepository @Inject constructor(
    private val dataSource: StudyFireStoreDataSource
){
    suspend fun fetchStudy(roadmapId: String?,questionId : String?, sectionId : Int?) = runCatching {
        dataSource.getStudyFireStore(roadmapId,questionId, sectionId) }
}