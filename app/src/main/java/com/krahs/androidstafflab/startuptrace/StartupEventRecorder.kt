package com.krahs.androidstafflab.startuptrace

import android.os.Process
import android.os.SystemClock
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.atomic.AtomicReferenceArray
import kotlin.math.max

enum class StartupEventKind(val label: String) {
    PROVIDER_ON_CREATE("StartupTraceProvider.onCreate()"),
    APPLICATION_ON_CREATE("Application.onCreate()"),
    ACTIVITY_ON_CREATE("MainActivity.onCreate()"),
    COMPOSE_CONTENT_ENTERED("Compose content entered"),
    ACTIVITY_ON_START("MainActivity.onStart()"),
    ACTIVITY_ON_RESUME("MainActivity.onResume()"),
    FIRST_FRAME_OBSERVED("First frame observed"),
}

data class StartupEventRecord(
    val kind: StartupEventKind,
    val elapsedRealtimeNanos: Long,
    val processId: Int,
    val threadName: String,
)

internal class StartupEventStore(
    private val clockNanos: () -> Long,
    private val processId: Int,
    private val threadName: () -> String,
) {
    private val records = AtomicReferenceArray<StartupEventRecord?>(StartupEventKind.entries.size)
    private val seenKinds = AtomicLong(0L)
    private val lastTimestamp = AtomicLong(Long.MIN_VALUE)

    fun recordOnce(kind: StartupEventKind) {
        val bit = 1L shl kind.ordinal
        while (true) {
            val seen = seenKinds.get()
            if (seen and bit != 0L) return
            if (seenKinds.compareAndSet(seen, seen or bit)) break
        }

        records.set(
            kind.ordinal,
            StartupEventRecord(
                kind = kind,
                elapsedRealtimeNanos = nextMonotonicTimestamp(),
                processId = processId,
                threadName = threadName(),
            ),
        )
    }

    fun snapshot(): List<StartupEventRecord> = buildList {
        StartupEventKind.entries.indices.forEach { index ->
            records.get(index)?.let(::add)
        }
    }.sortedBy(StartupEventRecord::elapsedRealtimeNanos)

    private fun nextMonotonicTimestamp(): Long {
        while (true) {
            val previous = lastTimestamp.get()
            val observed = clockNanos()
            val candidate = if (previous == Long.MAX_VALUE) {
                previous
            } else {
                max(observed, previous + 1L)
            }
            if (lastTimestamp.compareAndSet(previous, candidate)) return candidate
        }
    }
}

object StartupEventRecorder {
    private val store = StartupEventStore(
        clockNanos = SystemClock::elapsedRealtimeNanos,
        processId = Process.myPid(),
        threadName = { Thread.currentThread().name },
    )

    fun recordOnce(kind: StartupEventKind) = store.recordOnce(kind)

    fun snapshot(): List<StartupEventRecord> = store.snapshot()
}
