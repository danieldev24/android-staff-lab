package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.krahs.androidstafflab.feature.startup.content.StartupLane
import com.krahs.androidstafflab.feature.startup.content.StartupStage
import com.krahs.androidstafflab.ui.designsystem.LabIconBadge
import com.krahs.androidstafflab.ui.theme.traceColors

@Composable
fun StartupTimeline(
    lanes: List<StartupLane>,
    stages: List<StartupStage>,
    selectedStageId: String,
    skippedStageIds: Set<String>,
    modeLabel: String,
    onStageSelected: (String) -> Unit,
    onViewSources: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        LaneMap(lanes = lanes, stages = stages)
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            stages.forEachIndexed { index, stage ->
                StartupStageRow(
                    stage = stage,
                    stageCount = stages.size,
                    isSelected = stage.id == selectedStageId,
                    isSkipped = stage.id in skippedStageIds,
                    modeLabel = modeLabel,
                    showConnector = index < stages.lastIndex,
                    onClick = { onStageSelected(stage.id) },
                    onViewSources = { onViewSources(stage.sourceIds) },
                )
            }
        }
    }
}

@Composable
private fun LaneMap(
    lanes: List<StartupLane>,
    stages: List<StartupStage>,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                modifier = Modifier.semantics { heading() },
                text = "Lane map",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
            )
            lanes.forEach { lane ->
                val laneColor = lane.color()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("startup-lane"),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape),
                    ) {
                        Canvas(modifier = Modifier.matchParentSize()) {
                            drawCircle(color = laneColor)
                        }
                    }
                    Text(
                        modifier = Modifier.weight(1f),
                        text = lane.label,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    Text(
                        text = stages.count { it.lane == lane }.let { count ->
                            "$count ${if (count == 1) "stage" else "stages"}"
                        },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
        }
    }
}

@Composable
private fun StartupStageRow(
    stage: StartupStage,
    stageCount: Int,
    isSelected: Boolean,
    isSkipped: Boolean,
    modeLabel: String,
    showConnector: Boolean,
    onClick: () -> Unit,
    onViewSources: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .testTag("startup-stage"),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        StageRail(
            order = stage.order,
            color = stage.lane.color(),
            showConnector = showConnector,
        )
        Surface(
            onClick = onClick,
            modifier = Modifier
                .weight(1f)
                .defaultMinSize(minHeight = 48.dp)
                .testTag("startup-stage-${stage.id}")
                .semantics {
                    selected = isSelected
                    stateDescription = when {
                        isSkipped && isSelected -> "Skipped in $modeLabel · selected"
                        isSkipped -> "Skipped in $modeLabel"
                        isSelected -> "Current stage"
                        else -> "Pending"
                    }
                    contentDescription = "Stage ${stage.order} of $stageCount, ${stage.title}, ${stage.lane.label}"
                },
            shape = MaterialTheme.shapes.medium,
            color = when {
                isSelected -> MaterialTheme.colorScheme.primaryContainer
                isSkipped -> MaterialTheme.colorScheme.surfaceVariant
                else -> MaterialTheme.colorScheme.surface
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            shadowElevation = if (isSelected) 6.dp else 2.dp,
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .width(6.dp)
                        .fillMaxHeight(),
                    color = stage.lane.color(),
                    content = {},
                )
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(
                        text = stage.lane.label.uppercase(),
                        color = stage.lane.color(),
                        style = MaterialTheme.typography.labelSmall,
                    )
                    if (isSkipped) {
                        Text(
                            text = "SKIPPED IN ${modeLabel.uppercase()}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    Text(
                        text = stage.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        text = stage.summary,
                        color = if (isSelected) {
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f)
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    if (isSelected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        LearningLayer(
                            label = "What happens",
                            body = stage.whatHappens,
                        )
                        LearningLayer(
                            label = "Where it runs",
                            body = stage.whereItRuns,
                        )
                        LearningLayer(
                            label = "Why it matters",
                            body = stage.whyItMatters,
                        )
                        LearningLayer(
                            label = "Staff note",
                            body = stage.staffNote,
                            accentColor = MaterialTheme.colorScheme.secondary,
                        )
                        SourceIds(
                            sourceIds = stage.sourceIds,
                            onViewSources = onViewSources,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StageRail(
    order: Int,
    color: Color,
    showConnector: Boolean,
) {
    Column(
        modifier = Modifier.fillMaxHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LabIconBadge(
            symbol = order.toString().padStart(2, '0'),
            color = color,
        )
        if (showConnector) {
            Canvas(
                modifier = Modifier
                    .width(2.dp)
                    .weight(1f),
            ) {
                drawRect(color = color.copy(alpha = 0.34f))
            }
        }
    }
}

@Composable
private fun LearningLayer(
    label: String,
    body: String,
    accentColor: Color = MaterialTheme.colorScheme.primary,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = label,
            color = accentColor,
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            text = body,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun SourceIds(
    sourceIds: List<String>,
    onViewSources: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Official source IDs",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
        sourceIds.forEach { sourceId ->
            Text(
                text = "SOURCE · $sourceId",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        TextButton(onClick = onViewSources) {
            Text("Open official sources · ${sourceIds.size}")
        }
    }
}

@Composable
private fun StartupLane.color(): Color = when (this) {
    StartupLane.USER_LAUNCHER -> MaterialTheme.traceColors.user
    StartupLane.SYSTEM_SERVER -> MaterialTheme.traceColors.system
    StartupLane.ZYGOTE_USAP -> MaterialTheme.traceColors.runtime
    StartupLane.APP_MAIN_THREAD -> MaterialTheme.traceColors.app
    StartupLane.RENDER_PIPELINE -> MaterialTheme.traceColors.render
}
