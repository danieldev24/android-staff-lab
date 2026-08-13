package com.krahs.androidstafflab.navigation

import androidx.compose.ui.test.assertIsDisplayed
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
class AppNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun applicationStartupTopic_opensDetail_andSystemBackReturnsToLibrary() {
        composeTestRule
            .onNodeWithText("Android Staff Lab")
            .performScrollTo()
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("topic-category-android-platform")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("Android Platform")
            .assertIsDisplayed()
        composeTestRule
            .onNodeWithText("What happens when an Android application starts?")
            .performScrollTo()
            .assertIsDisplayed()

        composeTestRule
            .onNodeWithTag("topic-card-application-startup")
            .performScrollTo()
            .performClick()

        composeTestRule
            .onNodeWithText("What happens when an Android application starts?")
            .assertIsDisplayed()

        composeTestRule.activityRule.scenario.onActivity {
            it.onBackPressedDispatcher.onBackPressed()
        }
        composeTestRule.waitForIdle()

        composeTestRule
            .onNodeWithText("Android Staff Lab")
            .performScrollTo()
            .assertIsDisplayed()
    }

    @Test
    fun applicationStartupLesson_progressesThroughFourShortChapters() {
        composeTestRule
            .onNodeWithTag("topic-card-application-startup")
            .performScrollTo()
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
