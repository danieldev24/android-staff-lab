package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertHasClickAction
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
class StaffNotesTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun staffNotes_exposeOfficialSourcesInAccessibleSheet() {
        composeTestRule.onNodeWithTag("topic-card-application-startup").performClick()
        composeTestRule.onNodeWithTag("startup-lesson-evidence").performClick()
        composeTestRule.onNodeWithTag("startup-evidence-staff-notes").performClick()

        composeTestRule
            .onNodeWithText("Staff notes & caveats")
            .assertIsDisplayed()
        composeTestRule.onAllNodesWithTag("staff-note").assertCountEquals(6)

        composeTestRule
            .onNodeWithTag("staff-note-usap-is-conditional-sources")
            .performScrollTo()
            .assertHasClickAction()
            .performClick()

        composeTestRule.onNodeWithText("Official sources").assertIsDisplayed()
        composeTestRule.onNodeWithText("aosp-zygote").assertIsDisplayed()
        composeTestRule
            .onNodeWithTag("source-link-aosp-zygote")
            .assertHasClickAction()
    }
}
