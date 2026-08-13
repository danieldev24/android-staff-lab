package com.krahs.androidstafflab.startuptrace

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.krahs.androidstafflab.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StartupEventRecorderTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun coldLaunch_recordsContractualLifecycleOrderAndFirstFrame() {
        composeTestRule.waitForIdle()

        val records = StartupEventRecorder.snapshot()
        val orderedKinds = records.map(StartupEventRecord::kind)

        assertEquals(StartupEventKind.entries.size, records.size)
        assertTrue(records.zipWithNext().all { (first, second) ->
            first.elapsedRealtimeNanos <= second.elapsedRealtimeNanos
        })
        assertBefore(orderedKinds, StartupEventKind.PROVIDER_ON_CREATE, StartupEventKind.APPLICATION_ON_CREATE)
        assertBefore(orderedKinds, StartupEventKind.APPLICATION_ON_CREATE, StartupEventKind.ACTIVITY_ON_CREATE)
        assertBefore(orderedKinds, StartupEventKind.ACTIVITY_ON_CREATE, StartupEventKind.ACTIVITY_ON_START)
        assertBefore(orderedKinds, StartupEventKind.ACTIVITY_ON_START, StartupEventKind.ACTIVITY_ON_RESUME)
        assertBefore(orderedKinds, StartupEventKind.ACTIVITY_ON_RESUME, StartupEventKind.FIRST_FRAME_OBSERVED)
        assertBefore(orderedKinds, StartupEventKind.ACTIVITY_ON_CREATE, StartupEventKind.COMPOSE_CONTENT_ENTERED)
        assertBefore(orderedKinds, StartupEventKind.COMPOSE_CONTENT_ENTERED, StartupEventKind.FIRST_FRAME_OBSERVED)
    }

    private fun assertBefore(
        kinds: List<StartupEventKind>,
        first: StartupEventKind,
        second: StartupEventKind,
    ) {
        assertTrue("Expected $first before $second, actual: $kinds", kinds.indexOf(first) < kinds.indexOf(second))
    }
}
