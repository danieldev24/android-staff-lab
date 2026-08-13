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
class StartupEventLogUiTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun topicScreen_showsSevenObservedEventsWithContextAndObserverCaveat() {
        composeTestRule
            .onNodeWithTag("topic-card-application-startup")
            .performScrollTo()
            .performClick()
        composeTestRule.onNodeWithTag("startup-lesson-evidence").performClick()

        composeTestRule
            .onNodeWithText("Observed startup event log")
            .assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("startup-event-record").assertCountEquals(7)
        composeTestRule.onNodeWithText("Process / thread").assertExists()
        composeTestRule
            .onNodeWithText("Instrumentation changes the run it observes", substring = true)
            .assertExists()
    }
}
