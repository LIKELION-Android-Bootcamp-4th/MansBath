package com.aspa.aspa.features.state

sealed interface UiState<out T> {

    object Idle : UiState<Nothing>

    object Loading : UiState<Nothing>

    data class Success<T>(val data : T) : UiState<T>

    data class Failure(
        val message: String? = null,
        val throwable: Throwable? = null
    ) : UiState<Nothing>


    }
