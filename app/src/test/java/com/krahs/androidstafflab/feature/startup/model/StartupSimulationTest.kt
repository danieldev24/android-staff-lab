package com.krahs.androidstafflab.feature.startup.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class StartupSimulationTest {
    @Test
    fun baseline_hasDeterministicTtidAndTtfd() {
        val result = StartupSimulation.calculate(StartupSimulationState())

        assertEquals(320, result.ttidMs)
        assertEquals(480, result.ttfdMs)
    }

    @Test
    fun blockingWorkloads_affectBothTtidAndTtfd() {
        val expectedMetrics = mapOf(
            StartupWorkloadId.PROVIDER_SDK_AUTO_INIT to StartupMetrics(400, 560),
            StartupWorkloadId.APPLICATION_DISK_READ to StartupMetrics(440, 600),
            StartupWorkloadId.HEAVY_INITIAL_COMPOSITION to StartupMetrics(480, 640),
        )

        expectedMetrics.forEach { (workloadId, expected) ->
            val state = StartupSimulation.toggle(
                state = StartupSimulationState(),
                workloadId = workloadId,
            )

            assertEquals(expected, StartupSimulation.calculate(state))
        }
    }

    @Test
    fun deferredRequiredData_affectsTtfdButNotTtid() {
        val state = StartupSimulation.toggle(
            state = StartupSimulationState(),
            workloadId = StartupWorkloadId.DEFERRED_REQUIRED_DATA,
        )

        val result = StartupSimulation.calculate(state)
        assertEquals(320, result.ttidMs)
        assertEquals(700, result.ttfdMs)
    }

    @Test
    fun togglingWorkloadTwice_restoresOriginalScenario() {
        val enabled = StartupSimulation.toggle(
            StartupSimulationState(),
            StartupWorkloadId.APPLICATION_DISK_READ,
        )
        val disabled = StartupSimulation.toggle(
            enabled,
            StartupWorkloadId.APPLICATION_DISK_READ,
        )

        assertTrue(enabled.isEnabled(StartupWorkloadId.APPLICATION_DISK_READ))
        assertFalse(disabled.isEnabled(StartupWorkloadId.APPLICATION_DISK_READ))
        assertEquals(StartupSimulationState(), disabled)
    }

    @Test
    fun staffFixes_produceDeterministicBeforeAndAfterMetrics() {
        val allEnabled = StartupWorkloadId.entries.fold(StartupSimulationState()) { state, id ->
            StartupSimulation.toggle(state, id)
        }

        val comparison = StartupSimulation.compare(allEnabled)

        assertEquals(StartupMetrics(ttidMs = 680, ttfdMs = 1_060), comparison.before)
        assertEquals(StartupMetrics(ttidMs = 360, ttfdMs = 760), comparison.after)
        assertEquals(320, comparison.ttidSavedMs)
        assertEquals(300, comparison.ttfdSavedMs)
    }
}
