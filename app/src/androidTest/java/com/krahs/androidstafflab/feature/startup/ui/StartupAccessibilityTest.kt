package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.krahs.androidstafflab.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupAccessibilityTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun denseControls_have48DpTargetsAndActionSemantics() {
        openApplicationStartupTopic()

        composeTestRule.onNodeWithTag("startup-lesson-flow").performClick()
        listOf(
            "startup-control-previous",
            "startup-control-next",
            "startup-control-play-pause",
            "startup-control-reset",
        ).forEach { tag ->
            composeTestRule
                .onNodeWithTag(tag)
                .performScrollTo()
                .assertHeightIsAtLeast(48.dp)
                .assertHasClickAction()
        }

        composeTestRule.onNodeWithTag("startup-lesson-lab").performClick()
        listOf(
            "startup-lab-run",
            "startup-lab-apply-fixes",
            "startup-lab-reset",
        ).forEach { tag ->
            composeTestRule
                .onNodeWithTag(tag)
                .performScrollTo()
                .assertHeightIsAtLeast(48.dp)
                .assertHasClickAction()
        }
    }

    @Test
    fun timelineWorkloadAndEvent_exposeOrderLaneStateAndRole() {
        openApplicationStartupTopic()

        composeTestRule.onNodeWithTag("startup-lesson-flow").performClick()
        composeTestRule
            .onNodeWithTag("startup-stage-launch-request")
            .assertContentDescriptionEquals(
                "Stage 1 of 9, Launch request, User / Launcher",
            )
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.StateDescription,
                    "Current stage",
                ),
            )

        composeTestRule.onNodeWithTag("startup-lesson-lab").performClick()
        composeTestRule
            .onNodeWithTag("startup-workload-provider-sdk-auto-init")
            .performScrollTo()
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.Role,
                    Role.Switch,
                ),
            )
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.ToggleableState,
                    ToggleableState.Off,
                ),
            )

        composeTestRule.onNodeWithTag("startup-lesson-evidence").performClick()
        composeTestRule
            .onAllNodesWithTag("startup-event-record")[0]
            .performScrollTo()
            .assertContentDescriptionEquals(
                "Event 1 of 7, StartupTraceProvider.onCreate(), PID ${android.os.Process.myPid()}, thread main, +0.000 ms",
            )
    }

    private fun openApplicationStartupTopic() {
        composeTestRule
            .onNodeWithTag("topic-card-application-startup")
            .performClick()
    }
}
