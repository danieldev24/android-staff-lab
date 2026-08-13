package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.krahs.androidstafflab.feature.startup.content.StartupContent
import com.krahs.androidstafflab.feature.startup.model.StartupFlowAction
import com.krahs.androidstafflab.feature.startup.model.StartupFlowUiState
import com.krahs.androidstafflab.feature.startup.model.StartupMode
import com.krahs.androidstafflab.ui.theme.AndroidStaffLabTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class StartupReducedMotionTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun reducedMotion_replacesAutoplayWithExplicitStep() {
        val stages = StartupContent.coldStartStages
        val actions = mutableListOf<StartupFlowAction>()

        composeTestRule.setContent {
            AndroidStaffLabTheme {
                StartupFlowPlayer(
                    state = StartupFlowUiState(
                        mode = StartupMode.COLD,
                        currentStageId = stages.first().id,
                        activeStageIds = stages.mapTo(mutableSetOf()) { it.id },
                        isPlaying = false,
                    ),
                    stages = stages,
                    onAction = actions::add,
                    reducedMotion = true,
                )
            }
        }

        composeTestRule
            .onNodeWithTag("startup-control-play-pause")
            .assertTextEquals("Step")
            .performClick()

        assertEquals(listOf(StartupFlowAction.Next), actions)
    }
}
