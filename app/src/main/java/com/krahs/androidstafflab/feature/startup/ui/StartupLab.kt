package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.krahs.androidstafflab.feature.startup.model.StartupComparison
import com.krahs.androidstafflab.feature.startup.model.StartupMetrics
import com.krahs.androidstafflab.feature.startup.model.StartupSimulation
import com.krahs.androidstafflab.feature.startup.model.StartupSimulationAction
import com.krahs.androidstafflab.feature.startup.model.StartupSimulationState
import com.krahs.androidstafflab.feature.startup.model.StartupWorkload

@Composable
fun StartupLab(
    state: StartupSimulationState,
    onAction: (StartupSimulationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val metrics = StartupSimulation.calculate(state)
    val comparison = StartupSimulation.compare(state)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = "Critical-path lab",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                )
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        text = "Educational model — not a device benchmark",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Text(
                    text = "Bật workload để xem phần việc nào chặn first frame (TTID) và phần nào chỉ trì hoãn trạng thái usable (TTFD). Không workload thật nào được thực thi.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                StartupSimulation.workloads.forEach { workload ->
                    WorkloadToggle(
                        workload = workload,
                        enabled = state.isEnabled(workload.id),
                        onClick = {
                            onAction(StartupSimulationAction.ToggleWorkload(workload.id))
                        },
                    )
                }
            }

            Button(
                onClick = { onAction(StartupSimulationAction.Run) },
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 48.dp)
                    .testTag("startup-lab-run"),
            ) {
                Text("Run simulation")
            }

            if (state.hasRun) {
                CurrentMetrics(metrics = metrics)
            }

            Button(
                onClick = { onAction(StartupSimulationAction.ApplyStaffFixes) },
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 48.dp)
                    .testTag("startup-lab-apply-fixes"),
            ) {
                Text("Apply staff-level fixes")
            }
            OutlinedButton(
                onClick = { onAction(StartupSimulationAction.Reset) },
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 48.dp)
                    .testTag("startup-lab-reset"),
            ) {
                Text("Reset lab")
            }

            if (state.showComparison) {
                ComparisonCard(comparison = comparison)
            }
        }
    }
}

@Composable
private fun CurrentMetrics(metrics: StartupMetrics) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Current model",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MetricValue(
                    label = "TTID ${metrics.ttidMs} ms",
                    supporting = "first frame",
                    modifier = Modifier.weight(1f),
                )
                MetricValue(
                    label = "TTFD ${metrics.ttfdMs} ms",
                    supporting = "fully usable",
                    modifier = Modifier.weight(1f),
                )
            }
            MetricTrack(metrics = metrics)
        }
    }
}

@Composable
private fun MetricValue(
    label: String,
    supporting: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = supporting,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Composable
private fun MetricTrack(metrics: StartupMetrics) {
    val ttidFraction = (metrics.ttidMs.toFloat() / metrics.ttfdMs).coerceIn(0f, 1f)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(14.dp)
            .background(
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = MaterialTheme.shapes.small,
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(ttidFraction)
                .height(14.dp)
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = MaterialTheme.shapes.small,
                ),
        )
    }
}

@Composable
private fun WorkloadToggle(
    workload: StartupWorkload,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Column(modifier = Modifier.testTag("startup-workload")) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .sizeIn(minHeight = 48.dp)
                .testTag("startup-workload-${workload.id.name.lowercase().replace('_', '-')}")
                .semantics {
                    stateDescription = if (enabled) "Enabled" else "Disabled"
                },
            shape = MaterialTheme.shapes.medium,
            color = if (enabled) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = workload.title,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = workload.location,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = workload.effect,
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                Switch(
                    checked = enabled,
                    onCheckedChange = null,
                )
            }
        }
    }
}

@Composable
private fun ComparisonCard(comparison: StartupComparison) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "Before / after",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Before · TTID ${comparison.before.ttidMs} ms · TTFD ${comparison.before.ttfdMs} ms",
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "After · TTID ${comparison.after.ttidMs} ms · TTFD ${comparison.after.ttfdMs} ms",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = "Saved ${comparison.ttidSavedMs} ms before first frame · ${comparison.ttfdSavedMs} ms before usable",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.82f),
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
