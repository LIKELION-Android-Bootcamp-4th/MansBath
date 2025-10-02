package com.aspa2025.aspa2025.data.repository

import com.aspa2025.aspa2025.data.remote.StudyRemoteDataSource
import javax.inject.Inject

class StudyRepository @Inject constructor(
    private val studyRemote : StudyRemoteDataSource
) {
    suspend fun fetchStudy() = runCatching { studyRemote.getStudyData() }
}