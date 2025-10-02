package com.aspa2025.aspa2025.features.state

sealed class MakeQuizState {
    data object Navigate: MakeQuizState()
    data object Waiting: MakeQuizState()
}