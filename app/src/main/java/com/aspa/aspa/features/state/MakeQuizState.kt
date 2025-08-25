package com.aspa.aspa.features.state

sealed class MakeQuizState {
    data object Navigate: MakeQuizState()
    data object Idle: MakeQuizState()
}