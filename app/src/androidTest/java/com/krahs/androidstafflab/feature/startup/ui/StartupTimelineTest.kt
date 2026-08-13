package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performSemanticsAction
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.semantics.SemanticsActions
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.krahs.androidstafflab.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupTimelineTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun coldTimeline_rendersFiveLanesAndAllOrderedStages() {
        openApplicationStartupTopic()

        composeTestRule
            .onAllNodesWithTag("startup-lane")
            .assertCountEquals(5)

        composeTestRule
            .onAllNodesWithTag("startup-stage")
            .assertCountEquals(9)

        expectedStageIds.forEach { stageId ->
            composeTestRule
                .onNodeWithTag("startup-stage-$stageId")
                .assertExists()
        }
    }

    @Test
    fun selectingProviderStage_exposesLearningLayersAndStableSourceIds() {
        openApplicationStartupTopic()

        composeTestRule
            .onNodeWithTag("startup-stage-create-application-and-providers")
            .performScrollTo()
            .performSemanticsAction(SemanticsActions.OnClick)
            .assertIsSelected()

        listOf(
            "What happens",
            "Where it runs",
            "Why it matters",
            "Staff note",
        ).forEach { learningLayer ->
            composeTestRule
                .onNodeWithText(learningLayer, useUnmergedTree = true)
                .performScrollTo()
                .assertIsDisplayed()
        }

        listOf(
            "android-application-on-create",
            "android-content-provider",
            "android-app-startup",
        ).forEach { sourceId ->
            composeTestRule
                .onNodeWithText(sourceId, substring = true, useUnmergedTree = true)
                .performScrollTo()
                .assertIsDisplayed()
        }
    }

    private fun openApplicationStartupTopic() {
        composeTestRule
            .onNodeWithTag("topic-card-application-startup")
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithTag("startup-lesson-flow")
            .performClick()

        composeTestRule
            .onNodeWithText("9-stage trace")
            .performScrollTo()
            .assertIsDisplayed()
    }

    private companion object {
        val expectedStageIds = listOf(
            "launch-request",
            "resolve-and-schedule",
            "fork-or-specialize",
            "prepare-app-process",
            "create-application-and-providers",
            "application-on-create",
            "activity-lifecycle",
            "compose-phases",
            "first-frame-and-fully-drawn",
        )
    }
}
