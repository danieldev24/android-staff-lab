package com.krahs.androidstafflab.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.krahs.androidstafflab.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun applicationStartupTopic_opensDetail_andSystemBackReturnsToLibrary() {
        // RED before Task 5: this first assertion failed because the app opened detail directly.
        composeTestRule
            .onNodeWithText("Topic library")
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("topic-card-application-startup")
            .performClick()

        composeTestRule
            .onNodeWithText("What happens when an Android application starts?")
            .assertIsDisplayed()

        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Topic library")
            .assertIsDisplayed()
    }

    @Test
    fun applicationStartupLesson_progressesThroughFourShortChapters() {
        composeTestRule
            .onNodeWithTag("topic-card-application-startup")
            .performClick()

        composeTestRule.onNodeWithText("Build the mental model").assertIsDisplayed()

        listOf(
            "Follow one launch",
            "Shape the critical path",
            "Compare model with reality",
        ).forEach { lessonTitle ->
            composeTestRule.onNodeWithTag("startup-lesson-next").performClick()
            composeTestRule.onNodeWithText(lessonTitle).assertIsDisplayed()
        }

        composeTestRule.onNodeWithTag("startup-lesson-next").performClick()
        composeTestRule.onNodeWithTag("startup-lesson-complete").assertIsDisplayed()
    }
}
