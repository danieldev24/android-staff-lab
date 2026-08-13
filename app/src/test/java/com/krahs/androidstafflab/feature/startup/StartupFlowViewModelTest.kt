package com.krahs.androidstafflab.feature.startup

import com.krahs.androidstafflab.feature.startup.content.StartupContent
import com.krahs.androidstafflab.feature.startup.model.StartupFlowAction
import com.krahs.androidstafflab.feature.startup.model.StartupMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StartupFlowViewModelTest {
    @Test
    fun selectingWarmMode_resetsPlaybackAndMarksProcessCreationStagesSkipped() {
        val viewModel = StartupFlowViewModel()

        viewModel.onAction(StartupFlowAction.PlayPause)
        viewModel.onAction(StartupFlowAction.SelectMode(StartupMode.WARM))

        val state = viewModel.uiState.value
        assertEquals(StartupMode.WARM, state.mode)
        assertEquals("launch-request", state.currentStageId)
        assertFalse(state.isPlaying)
        assertTrue(state.isSkipped("fork-or-specialize"))
        assertTrue(state.isSkipped("prepare-app-process"))
        assertTrue(state.isSkipped("create-application-and-providers"))
        assertTrue(state.isSkipped("application-on-create"))
        assertFalse(state.isSkipped("activity-lifecycle"))
    }

    @Test
    fun nextAndPrevious_followActiveStagesWithoutExceedingBounds() {
        val viewModel = StartupFlowViewModel()
        viewModel.onAction(StartupFlowAction.SelectMode(StartupMode.WARM))

        viewModel.onAction(StartupFlowAction.Previous)
        assertEquals("launch-request", viewModel.uiState.value.currentStageId)

        viewModel.onAction(StartupFlowAction.Next)
        assertEquals("resolve-and-schedule", viewModel.uiState.value.currentStageId)

        viewModel.onAction(StartupFlowAction.Next)
        assertEquals("activity-lifecycle", viewModel.uiState.value.currentStageId)

        viewModel.onAction(
            StartupFlowAction.SelectStage(StartupContent.coldStartStages.last().id),
        )
        viewModel.onAction(StartupFlowAction.Next)
        assertEquals("first-frame-and-fully-drawn", viewModel.uiState.value.currentStageId)
    }

    @Test
    fun hotMode_keepsOnlySystemHandoffAndFirstFrameActive() {
        val viewModel = StartupFlowViewModel()

        viewModel.onAction(StartupFlowAction.SelectMode(StartupMode.HOT))

        val state = viewModel.uiState.value
        assertEquals(
            setOf(
                "launch-request",
                "resolve-and-schedule",
                "first-frame-and-fully-drawn",
            ),
            state.activeStageIds,
        )
        assertTrue(state.isSkipped("activity-lifecycle"))
        assertTrue(state.isSkipped("compose-phases"))
    }

    @Test
    fun playback_restartsAtFirstStageAndStopsOnFinalStage() {
        val viewModel = StartupFlowViewModel()
        viewModel.onAction(
            StartupFlowAction.SelectStage(StartupContent.coldStartStages.last().id),
        )

        viewModel.onAction(StartupFlowAction.PlayPause)
        assertEquals("launch-request", viewModel.uiState.value.currentStageId)
        assertTrue(viewModel.uiState.value.isPlaying)

        viewModel.onAction(
            StartupFlowAction.SelectStage("compose-phases"),
        )
        viewModel.onAction(StartupFlowAction.PlayPause)
        viewModel.onAction(StartupFlowAction.PlaybackTick)

        assertEquals("first-frame-and-fully-drawn", viewModel.uiState.value.currentStageId)
        assertFalse(viewModel.uiState.value.isPlaying)
    }

    @Test
    fun reset_returnsToFirstActiveStageAndStopsPlayback() {
        val viewModel = StartupFlowViewModel()
        viewModel.onAction(StartupFlowAction.Next)
        viewModel.onAction(StartupFlowAction.PlayPause)

        viewModel.onAction(StartupFlowAction.Reset)

        val state = viewModel.uiState.value
        assertEquals("launch-request", state.currentStageId)
        assertFalse(state.isPlaying)
    }
}
