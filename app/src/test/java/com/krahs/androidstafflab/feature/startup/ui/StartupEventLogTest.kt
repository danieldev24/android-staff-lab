package com.krahs.androidstafflab.feature.startup.ui

import com.krahs.androidstafflab.startuptrace.StartupEventKind
import com.krahs.androidstafflab.startuptrace.StartupEventRecord
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StartupEventLogTest {
    @Test
    fun toStartupEventRows_sortsRecordsAndUsesFirstObservationAsZero() {
        val records = listOf(
            record(StartupEventKind.ACTIVITY_ON_CREATE, 5_500_000L),
            record(StartupEventKind.PROVIDER_ON_CREATE, 1_000_000L),
            record(StartupEventKind.APPLICATION_ON_CREATE, 2_250_000L),
        )

        val rows = records.toStartupEventRows()

        assertEquals(
            listOf(
                StartupEventKind.PROVIDER_ON_CREATE,
                StartupEventKind.APPLICATION_ON_CREATE,
                StartupEventKind.ACTIVITY_ON_CREATE,
            ),
            rows.map(StartupEventRow::kind),
        )
        assertEquals(listOf(0L, 1_250_000L, 4_500_000L), rows.map(StartupEventRow::relativeNanos))
        assertTrue(rows.zipWithNext().all { (first, second) -> first.relativeNanos <= second.relativeNanos })
    }

    private fun record(kind: StartupEventKind, timestamp: Long) = StartupEventRecord(
        kind = kind,
        elapsedRealtimeNanos = timestamp,
        processId = 42,
        threadName = "main",
    )
}
