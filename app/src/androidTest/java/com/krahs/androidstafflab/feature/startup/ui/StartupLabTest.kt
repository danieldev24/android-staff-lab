package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
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
class StartupLabTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun workloadToggles_updateMetrics_andStaffFixesShowBeforeAfter() {
        openApplicationStartupTopic()

        composeTestRule
            .onAllNodesWithTag("startup-workload")
            .assertCountEquals(4)
        composeTestRule
            .onNodeWithText("Educational model — not a device benchmark")
            .performScrollTo()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("startup-lab-run")
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithText("TTID 320 ms")
            .performScrollTo()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("TTFD 480 ms")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("startup-workload-provider-sdk-auto-init")
            .performScrollTo()
            .performClick()
        composeTestRule
            .onNodeWithTag("startup-workload-deferred-required-data")
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithTag("startup-lab-run")
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithText("TTID 400 ms")
            .performScrollTo()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("TTFD 780 ms")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("startup-lab-apply-fixes")
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithText("Before · TTID 400 ms · TTFD 780 ms")
            .performScrollTo()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("After · TTID 320 ms · TTFD 600 ms")
            .performScrollTo()
            .assertIsDisplayed()
    }

    private fun openApplicationStartupTopic() {
        composeTestRule
            .onNodeWithTag("topic-card-application-startup")
            .performClick()

        composeTestRule
            .onNodeWithTag("startup-lesson-lab")
            .performClick()

        composeTestRule
            .onNodeWithText("Critical-path lab")
            .assertIsDisplayed()
    }
}
