package com.aspa.aspa.data.repository

import com.aspa.aspa.data.remote.MistakeDetailRemoteDataSource
import javax.inject.Inject

class MistakeDetailRepository @Inject constructor(
    val dataSource: MistakeDetailRemoteDataSource
) {
    suspend fun fetchMistakeDetail(mistakeId : String) = runCatching { dataSource.getMistakeAnswerDetail(mistakeId) }
}