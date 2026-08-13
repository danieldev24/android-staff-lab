package com.krahs.androidstafflab.startuptrace

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class StartupEventStoreTest {
    @Test
    fun recordOnce_keepsSevenUniqueEventsInMonotonicObservationOrder() {
        var nowNanos = 10_000L
        val store = StartupEventStore(
            clockNanos = {
                nowNanos += 100
                nowNanos
            },
            processId = 42,
            threadName = { "main" },
        )

        StartupEventKind.entries.forEach(store::recordOnce)
        store.recordOnce(StartupEventKind.APPLICATION_ON_CREATE)

        val records = store.snapshot()

        assertEquals(7, records.size)
        assertEquals(StartupEventKind.entries, records.map(StartupEventRecord::kind))
        assertTrue(records.zipWithNext().all { (first, second) ->
            first.elapsedRealtimeNanos <= second.elapsedRealtimeNanos
        })
        assertTrue(records.all { it.processId == 42 && it.threadName == "main" })
    }

    @Test
    fun snapshot_returnsAnImmutablePointInTimeCopy() {
        var nowNanos = 20_000L
        val store = StartupEventStore(
            clockNanos = { ++nowNanos },
            processId = 7,
            threadName = { "main" },
        )

        store.recordOnce(StartupEventKind.PROVIDER_ON_CREATE)
        val firstSnapshot = store.snapshot()
        store.recordOnce(StartupEventKind.APPLICATION_ON_CREATE)

        assertEquals(1, firstSnapshot.size)
        assertEquals(2, store.snapshot().size)
    }
}
