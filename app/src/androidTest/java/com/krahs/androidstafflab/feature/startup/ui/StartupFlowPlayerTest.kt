package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.krahs.androidstafflab.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupFlowPlayerTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun warmMode_skipsProcessStages_andPlayerControlsStayWithinBounds() {
        openApplicationStartupTopic()

        composeTestRule
            .onNodeWithTag("startup-mode-warm")
            .performScrollTo()
            .performClick()
            .assertIsSelected()

        composeTestRule
            .onNodeWithTag("startup-stage-fork-or-specialize")
            .assert(
                SemanticsMatcher.expectValue(
                    SemanticsProperties.StateDescription,
                    "Skipped in Warm",
                ),
            )

        composeTestRule
            .onNodeWithTag("startup-control-previous")
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithText("Stage 1 of 9 · Launch request")
            .assertIsDisplayed()

        repeat(2) {
            composeTestRule
                .onNodeWithTag("startup-control-next")
                .performScrollTo()
                .performClick()
        }
        composeTestRule
            .onNodeWithText("Stage 7 of 9 · Launch Activity")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("startup-control-reset")
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithText("Stage 1 of 9 · Launch request")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("startup-control-play-pause")
            .performClick()
        composeTestRule
            .onNodeWithText("Pause")
            .assertIsDisplayed()
    }

    private fun openApplicationStartupTopic() {
        composeTestRule
            .onNodeWithTag("topic-card-application-startup")
            .performClick()

        composeTestRule
            .onNodeWithTag("startup-lesson-flow")
            .performClick()

        composeTestRule
            .onNodeWithText("Startup flow player")
            .assertIsDisplayed()
    }
}
