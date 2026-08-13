package com.krahs.androidstafflab.feature.startup.model

import androidx.compose.runtime.Immutable
import kotlin.math.max

@Immutable
enum class StartupWorkloadId {
    PROVIDER_SDK_AUTO_INIT,
    APPLICATION_DISK_READ,
    HEAVY_INITIAL_COMPOSITION,
    DEFERRED_REQUIRED_DATA,
}

@Immutable
data class StartupWorkload(
    val id: StartupWorkloadId,
    val title: String,
    val location: String,
    val effect: String,
    val ttidCostMs: Int,
    val ttfdCostMs: Int,
    val optimizedTtidCostMs: Int,
    val optimizedTtfdCostMs: Int,
)

@Immutable
data class StartupSimulationState(
    val providerSdkAutoInitEnabled: Boolean = false,
    val applicationDiskReadEnabled: Boolean = false,
    val heavyInitialCompositionEnabled: Boolean = false,
    val deferredRequiredDataEnabled: Boolean = false,
    val hasRun: Boolean = false,
    val showComparison: Boolean = false,
) {
    fun isEnabled(id: StartupWorkloadId): Boolean = when (id) {
        StartupWorkloadId.PROVIDER_SDK_AUTO_INIT -> providerSdkAutoInitEnabled
        StartupWorkloadId.APPLICATION_DISK_READ -> applicationDiskReadEnabled
        StartupWorkloadId.HEAVY_INITIAL_COMPOSITION -> heavyInitialCompositionEnabled
        StartupWorkloadId.DEFERRED_REQUIRED_DATA -> deferredRequiredDataEnabled
    }
}

@Immutable
data class StartupMetrics(
    val ttidMs: Int,
    val ttfdMs: Int,
)

@Immutable
data class StartupComparison(
    val before: StartupMetrics,
    val after: StartupMetrics,
) {
    val ttidSavedMs: Int = before.ttidMs - after.ttidMs
    val ttfdSavedMs: Int = before.ttfdMs - after.ttfdMs
}

sealed interface StartupSimulationAction {
    @Immutable
    data class ToggleWorkload(val workloadId: StartupWorkloadId) : StartupSimulationAction

    data object Run : StartupSimulationAction
    data object ApplyStaffFixes : StartupSimulationAction
    data object Reset : StartupSimulationAction
}

object StartupSimulation {
    private const val BASE_TTID_MS = 320
    private const val BASE_TTFD_MS = 480

    val workloads: List<StartupWorkload> = listOf(
        StartupWorkload(
            id = StartupWorkloadId.PROVIDER_SDK_AUTO_INIT,
            title = "SDK auto-init in ContentProvider",
            location = "Before Application.onCreate() · main thread",
            effect = "+80 ms TTID / TTFD",
            ttidCostMs = 80,
            ttfdCostMs = 80,
            optimizedTtidCostMs = 0,
            optimizedTtfdCostMs = 0,
        ),
        StartupWorkload(
            id = StartupWorkloadId.APPLICATION_DISK_READ,
            title = "Disk read in Application.onCreate()",
            location = "Startup critical path · main thread",
            effect = "+120 ms TTID / TTFD",
            ttidCostMs = 120,
            ttfdCostMs = 120,
            optimizedTtidCostMs = 0,
            optimizedTtfdCostMs = 120,
        ),
        StartupWorkload(
            id = StartupWorkloadId.HEAVY_INITIAL_COMPOSITION,
            title = "Heavy initial composition",
            location = "Activity / Compose · before first frame",
            effect = "+160 ms TTID / TTFD",
            ttidCostMs = 160,
            ttfdCostMs = 160,
            optimizedTtidCostMs = 40,
            optimizedTtfdCostMs = 40,
        ),
        StartupWorkload(
            id = StartupWorkloadId.DEFERRED_REQUIRED_DATA,
            title = "Deferred required data",
            location = "After first frame · required for usable state",
            effect = "+0 ms TTID / +220 ms TTFD",
            ttidCostMs = 0,
            ttfdCostMs = 220,
            optimizedTtidCostMs = 0,
            optimizedTtfdCostMs = 120,
        ),
    )

    fun toggle(
        state: StartupSimulationState,
        workloadId: StartupWorkloadId,
    ): StartupSimulationState = when (workloadId) {
        StartupWorkloadId.PROVIDER_SDK_AUTO_INIT -> state.copy(
            providerSdkAutoInitEnabled = !state.providerSdkAutoInitEnabled,
            hasRun = false,
            showComparison = false,
        )

        StartupWorkloadId.APPLICATION_DISK_READ -> state.copy(
            applicationDiskReadEnabled = !state.applicationDiskReadEnabled,
            hasRun = false,
            showComparison = false,
        )

        StartupWorkloadId.HEAVY_INITIAL_COMPOSITION -> state.copy(
            heavyInitialCompositionEnabled = !state.heavyInitialCompositionEnabled,
            hasRun = false,
            showComparison = false,
        )

        StartupWorkloadId.DEFERRED_REQUIRED_DATA -> state.copy(
            deferredRequiredDataEnabled = !state.deferredRequiredDataEnabled,
            hasRun = false,
            showComparison = false,
        )
    }

    fun calculate(
        state: StartupSimulationState,
        optimized: Boolean = false,
    ): StartupMetrics {
        val enabledWorkloads = workloads.filter { state.isEnabled(it.id) }
        val ttidCost = enabledWorkloads.sumOf { workload ->
            if (optimized) workload.optimizedTtidCostMs else workload.ttidCostMs
        }
        val ttfdCost = enabledWorkloads.sumOf { workload ->
            if (optimized) workload.optimizedTtfdCostMs else workload.ttfdCostMs
        }
        val ttid = BASE_TTID_MS + ttidCost

        return StartupMetrics(
            ttidMs = ttid,
            ttfdMs = max(ttid, BASE_TTFD_MS + ttfdCost),
        )
    }

    fun compare(state: StartupSimulationState): StartupComparison = StartupComparison(
        before = calculate(state),
        after = calculate(state, optimized = true),
    )
}
