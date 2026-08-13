package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.krahs.androidstafflab.startuptrace.StartupEventKind
import com.krahs.androidstafflab.startuptrace.StartupEventRecord
import com.krahs.androidstafflab.ui.designsystem.LabOrganicPanel
import com.krahs.androidstafflab.ui.designsystem.LabPill
import com.krahs.androidstafflab.ui.designsystem.LabSectionHeader
import java.util.Locale

@Immutable
data class StartupEventRow(
    val kind: StartupEventKind,
    val relativeNanos: Long,
    val processId: Int,
    val threadName: String,
)

internal fun List<StartupEventRecord>.toStartupEventRows(): List<StartupEventRow> {
    val ordered = sortedBy(StartupEventRecord::elapsedRealtimeNanos)
    val baseline = ordered.firstOrNull()?.elapsedRealtimeNanos ?: return emptyList()
    return ordered.map { record ->
        StartupEventRow(
            kind = record.kind,
            relativeNanos = (record.elapsedRealtimeNanos - baseline).coerceAtLeast(0L),
            processId = record.processId,
            threadName = record.threadName,
        )
    }
}

@Composable
fun StartupEventLog(
    records: List<StartupEventRecord>,
    modifier: Modifier = Modifier,
) {
    val rows = records.toStartupEventRows()
    LabOrganicPanel(modifier = modifier.fillMaxWidth()) {
        LabSectionHeader(
            title = "Observed startup event log",
            supportingLabel = "${rows.size} hooks",
        )
        Spacer(modifier = Modifier.height(12.dp))
        LabPill(
            label = "THIS RUN ONLY",
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "These timestamps describe this process launch, not every Android device or entry point.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Instrumentation changes the run it observes: the provider and hooks add a small observer cost. No hook performs disk/network I/O or sleeps.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            modifier = Modifier.semantics { heading() },
            text = "Process / thread",
            style = MaterialTheme.typography.titleMedium,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            rows.forEachIndexed { index, row ->
                StartupEventRecordRow(index = index, row = row)
            }
        }
    }
}

@Composable
private fun StartupEventRecordRow(
    index: Int,
    row: StartupEventRow,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("startup-event-record"),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier.size(38.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = (index + 1).toString().padStart(2, '0'),
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp),
            ) {
                Text(
                    text = row.kind.label,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "PID ${row.processId} · ${row.threadName}",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            Text(
                text = row.relativeNanos.asMillisecondsLabel(),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

private fun Long.asMillisecondsLabel(): String =
    String.format(Locale.US, "+%.3f ms", this / 1_000_000.0)
