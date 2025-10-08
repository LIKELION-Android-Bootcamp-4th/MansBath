package com.aspa2025.aspa2025.data.repository

import com.aspa2025.aspa2025.data.remote.MistakeDetailRemoteDataSource
import javax.inject.Inject

class MistakeDetailRepository @Inject constructor(
    val dataSource: MistakeDetailRemoteDataSource
) {
    suspend fun fetchMistakeDetail(mistakeId : String) = runCatching { dataSource.getMistakeAnswerDetail(mistakeId) }
}