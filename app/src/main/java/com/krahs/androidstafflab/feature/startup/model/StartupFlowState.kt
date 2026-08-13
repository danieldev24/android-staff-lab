package com.krahs.androidstafflab.feature.startup.model

import androidx.compose.runtime.Immutable

@Immutable
enum class StartupMode(val label: String) {
    COLD("Cold"),
    WARM("Warm"),
    HOT("Hot"),
}

@Immutable
data class StartupFlowUiState(
    val mode: StartupMode,
    val currentStageId: String,
    val activeStageIds: Set<String>,
    val isPlaying: Boolean,
) {
    fun isSkipped(stageId: String): Boolean = stageId !in activeStageIds
}

sealed interface StartupFlowAction {
    @Immutable
    data class SelectMode(val mode: StartupMode) : StartupFlowAction

    @Immutable
    data class SelectStage(val stageId: String) : StartupFlowAction

    data object PlayPause : StartupFlowAction
    data object Previous : StartupFlowAction
    data object Next : StartupFlowAction
    data object Reset : StartupFlowAction
    data object PlaybackTick : StartupFlowAction
}
