package com.aspa.aspa.data.repository

import com.aspa.aspa.data.remote.StudyRemoteDataSource
import javax.inject.Inject

class StudyRepository @Inject constructor(
    private val studyRemote : StudyRemoteDataSource
) {
    suspend fun fetchStudy() = runCatching { studyRemote.getStudyData() }
}