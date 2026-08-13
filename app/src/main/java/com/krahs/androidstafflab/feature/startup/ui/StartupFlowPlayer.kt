package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.krahs.androidstafflab.feature.startup.content.StartupStage
import com.krahs.androidstafflab.feature.startup.model.StartupFlowAction
import com.krahs.androidstafflab.feature.startup.model.StartupFlowUiState
import com.krahs.androidstafflab.feature.startup.model.StartupMode

@Composable
fun StartupFlowPlayer(
    state: StartupFlowUiState,
    stages: List<StartupStage>,
    onAction: (StartupFlowAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentStage = stages.first { it.id == state.currentStageId }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = "Startup flow player",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Cold tạo process mới; Warm tái dùng process; Hot tái dùng cả process và Activity UI hiện có.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                StartupMode.entries.forEach { mode ->
                    FilterChip(
                        selected = state.mode == mode,
                        onClick = { onAction(StartupFlowAction.SelectMode(mode)) },
                        label = { Text(mode.label) },
                        modifier = Modifier
                            .weight(1f)
                            .sizeIn(minHeight = 48.dp)
                            .testTag("startup-mode-${mode.name.lowercase()}"),
                    )
                }
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("startup-current-stage"),
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        text = "Stage ${currentStage.order} of ${stages.size} · ${currentStage.title}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = "${state.activeStageIds.size} active · ${stages.size - state.activeStageIds.size} skipped in ${state.mode.label}",
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.76f),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                OutlinedButton(
                    onClick = { onAction(StartupFlowAction.Previous) },
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 48.dp)
                        .testTag("startup-control-previous"),
                ) {
                    Text("Previous")
                }
                OutlinedButton(
                    onClick = { onAction(StartupFlowAction.Next) },
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 48.dp)
                        .testTag("startup-control-next"),
                ) {
                    Text("Next")
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Button(
                    onClick = { onAction(StartupFlowAction.PlayPause) },
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 48.dp)
                        .testTag("startup-control-play-pause"),
                ) {
                    Text(if (state.isPlaying) "Pause" else "Play")
                }
                OutlinedButton(
                    onClick = { onAction(StartupFlowAction.Reset) },
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 48.dp)
                        .testTag("startup-control-reset"),
                ) {
                    Text("Reset")
                }
            }
        }
    }
}
