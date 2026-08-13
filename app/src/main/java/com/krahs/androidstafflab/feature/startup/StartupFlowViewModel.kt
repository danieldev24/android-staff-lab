package com.krahs.androidstafflab.feature.startup

import androidx.lifecycle.ViewModel
import com.krahs.androidstafflab.feature.startup.content.StartupContent
import com.krahs.androidstafflab.feature.startup.model.StartupFlowAction
import com.krahs.androidstafflab.feature.startup.model.StartupFlowUiState
import com.krahs.androidstafflab.feature.startup.model.StartupMode
import com.krahs.androidstafflab.feature.startup.model.StartupSimulation
import com.krahs.androidstafflab.feature.startup.model.StartupSimulationAction
import com.krahs.androidstafflab.feature.startup.model.StartupSimulationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class StartupFlowViewModel : ViewModel() {
    private val orderedStageIds = StartupContent.coldStartStages.map { it.id }

    private val _uiState = MutableStateFlow(initialState())
    val uiState: StateFlow<StartupFlowUiState> = _uiState.asStateFlow()

    private val _simulationState = MutableStateFlow(StartupSimulationState())
    val simulationState: StateFlow<StartupSimulationState> = _simulationState.asStateFlow()

    fun onAction(action: StartupFlowAction) {
        _uiState.update { state -> reduce(state, action) }
    }

    fun onSimulationAction(action: StartupSimulationAction) {
        _simulationState.update { state ->
            when (action) {
                is StartupSimulationAction.ToggleWorkload -> StartupSimulation.toggle(
                    state = state,
                    workloadId = action.workloadId,
                )

                StartupSimulationAction.Run -> state.copy(
                    hasRun = true,
                    showComparison = false,
                )

                StartupSimulationAction.ApplyStaffFixes -> state.copy(
                    hasRun = true,
                    showComparison = true,
                )
                StartupSimulationAction.Reset -> StartupSimulationState()
            }
        }
    }

    private fun reduce(
        state: StartupFlowUiState,
        action: StartupFlowAction,
    ): StartupFlowUiState = when (action) {
        is StartupFlowAction.SelectMode -> state.copy(
            mode = action.mode,
            currentStageId = orderedStageIds.first(),
            activeStageIds = activeStageIds(action.mode),
            isPlaying = false,
        )

        is StartupFlowAction.SelectStage -> if (action.stageId in orderedStageIds) {
            state.copy(currentStageId = action.stageId, isPlaying = false)
        } else {
            state
        }

        StartupFlowAction.PlayPause -> when {
            state.isPlaying -> state.copy(isPlaying = false)
            state.currentStageId == state.lastActiveStageId() -> state.copy(
                currentStageId = state.firstActiveStageId(),
                isPlaying = true,
            )

            else -> state.copy(isPlaying = true)
        }

        StartupFlowAction.Previous -> state.moveBy(direction = -1)
        StartupFlowAction.Next -> state.moveBy(direction = 1)
        StartupFlowAction.Reset -> state.copy(
            currentStageId = state.firstActiveStageId(),
            isPlaying = false,
        )

        StartupFlowAction.PlaybackTick -> state.playbackTick()
    }

    private fun initialState(): StartupFlowUiState = StartupFlowUiState(
        mode = StartupMode.COLD,
        currentStageId = orderedStageIds.first(),
        activeStageIds = activeStageIds(StartupMode.COLD),
        isPlaying = false,
    )

    private fun activeStageIds(mode: StartupMode): Set<String> = when (mode) {
        StartupMode.COLD -> orderedStageIds.toSet()
        StartupMode.WARM -> orderedStageIds
            .filterIndexed { index, _ -> index < 2 || index >= 6 }
            .toSet()

        StartupMode.HOT -> setOf(
            orderedStageIds[0],
            orderedStageIds[1],
            orderedStageIds.last(),
        )
    }

    private fun StartupFlowUiState.moveBy(direction: Int): StartupFlowUiState {
        val currentIndex = orderedStageIds.indexOf(currentStageId)
        val activeIndexes = orderedStageIds.indices.filter { orderedStageIds[it] in activeStageIds }
        val destinationIndex = if (direction < 0) {
            activeIndexes.lastOrNull { it < currentIndex }
        } else {
            activeIndexes.firstOrNull { it > currentIndex }
        } ?: currentIndex

        return copy(
            currentStageId = orderedStageIds[destinationIndex],
            isPlaying = false,
        )
    }

    private fun StartupFlowUiState.playbackTick(): StartupFlowUiState {
        if (!isPlaying) return this

        val currentIndex = orderedStageIds.indexOf(currentStageId)
        val nextIndex = orderedStageIds.indices.firstOrNull { index ->
            index > currentIndex && orderedStageIds[index] in activeStageIds
        } ?: return copy(isPlaying = false)
        val nextStageId = orderedStageIds[nextIndex]

        return copy(
            currentStageId = nextStageId,
            isPlaying = nextStageId != lastActiveStageId(),
        )
    }

    private fun StartupFlowUiState.firstActiveStageId(): String =
        orderedStageIds.first(activeStageIds::contains)

    private fun StartupFlowUiState.lastActiveStageId(): String =
        orderedStageIds.last(activeStageIds::contains)
}
