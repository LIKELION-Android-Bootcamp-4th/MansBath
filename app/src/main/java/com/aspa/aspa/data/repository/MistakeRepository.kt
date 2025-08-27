package com.aspa.aspa.data.repository

import com.aspa.aspa.data.remote.MistakeNotebookDataSource

import javax.inject.Inject

class MistakeRepository @Inject constructor(
    private val dataSource: MistakeNotebookDataSource
){
    suspend fun fetchMistakeAnswer() = runCatching {
        dataSource.fetchMistakeList()
    }
}
