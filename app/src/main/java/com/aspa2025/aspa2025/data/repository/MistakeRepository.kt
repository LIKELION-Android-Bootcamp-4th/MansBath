package com.aspa2025.aspa2025.data.repository

import com.aspa2025.aspa2025.data.remote.MistakeNotebookDataSource

import javax.inject.Inject

class MistakeRepository @Inject constructor(
    private val dataSource: MistakeNotebookDataSource
){
    suspend fun fetchMistakeAnswer() = runCatching {
        dataSource.fetchMistakeList()
    }
}
