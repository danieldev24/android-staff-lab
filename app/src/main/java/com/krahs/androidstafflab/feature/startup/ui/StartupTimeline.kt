package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.krahs.androidstafflab.feature.startup.content.StartupLane
import com.krahs.androidstafflab.feature.startup.content.StartupStage
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
    val selectedStage = stages.first { it.id == selectedStageId }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        LaneMap(lanes = lanes, stages = stages)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = "9-stage trace",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Swipe stages →",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                stages.forEach { stage ->
                    StartupStageCard(
                        stage = stage,
                        stageCount = stages.size,
                        isSelected = stage.id == selectedStageId,
                        isSkipped = stage.id in skippedStageIds,
                        modeLabel = modeLabel,
                        onClick = { onStageSelected(stage.id) },
                    )
                }
            }
        }
        SelectedStageLesson(
            stage = selectedStage,
            isSkipped = selectedStage.id in skippedStageIds,
            modeLabel = modeLabel,
            onViewSources = { onViewSources(selectedStage.sourceIds) },
        )
    }
}

@Composable
private fun LaneMap(
    lanes: List<StartupLane>,
    stages: List<StartupStage>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            modifier = Modifier.semantics { heading() },
            text = "Execution lanes",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            lanes.forEach { lane ->
                val laneColor = lane.color()
                Surface(
                    modifier = Modifier.testTag("startup-lane"),
                    shape = CircleShape,
                    color = laneColor.copy(alpha = 0.12f),
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape),
                        ) {
                            Canvas(modifier = Modifier.matchParentSize()) {
                                drawCircle(color = laneColor)
                            }
                        }
                        Text(
                            text = lane.label,
                            style = MaterialTheme.typography.labelLarge,
                        )
                        Text(
                            text = stages.count { it.lane == lane }.toString(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StartupStageCard(
    stage: StartupStage,
    stageCount: Int,
    isSelected: Boolean,
    isSkipped: Boolean,
    modeLabel: String,
    onClick: () -> Unit,
) {
    val stageColor = stage.lane.color()
    Box(modifier = Modifier.testTag("startup-stage")) {
        Surface(
            onClick = onClick,
            modifier = Modifier
                .widthIn(min = 132.dp, max = 148.dp)
                .heightIn(min = 112.dp)
                .alpha(if (isSkipped && !isSelected) 0.52f else 1f)
                .testTag("startup-stage-${stage.id}")
                .semantics {
                    selected = isSelected
                    stateDescription = when {
                        isSkipped && isSelected -> "Skipped in $modeLabel · selected"
                        isSkipped -> "Skipped in $modeLabel"
                        isSelected -> "Current stage"
                        else -> "Pending"
                    }
                    contentDescription =
                        "Stage ${stage.order} of $stageCount, ${stage.title}, ${stage.lane.label}"
                },
            shape = MaterialTheme.shapes.medium,
            color = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            },
            contentColor = if (isSelected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            shadowElevation = if (isSelected) 4.dp else 1.dp,
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(5.dp),
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawRect(color = stageColor)
                    }
                }
                Column(
                    modifier = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = stage.order.toString().padStart(2, '0'),
                        color = stageColor,
                        style = MaterialTheme.typography.labelLarge,
                    )
                    Text(
                        text = stage.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                    if (isSkipped) {
                        Text(
                            text = "SKIPPED",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SelectedStageLesson(
    stage: StartupStage,
    isSkipped: Boolean,
    modeLabel: String,
    onViewSources: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stage.order.toString().padStart(2, '0'),
                    color = stage.lane.color(),
                    style = MaterialTheme.typography.titleMedium,
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stage.lane.label.uppercase(),
                        color = stage.lane.color(),
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text(
                        modifier = Modifier.semantics { heading() },
                        text = stage.title,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                if (isSkipped) {
                    Text(
                        text = "Skipped in $modeLabel",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }
            Text(
                text = stage.summary,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
            LearningLayer(label = "What happens", body = stage.whatHappens)
            LearningLayer(label = "Where it runs", body = stage.whereItRuns)
            LearningLayer(label = "Why it matters", body = stage.whyItMatters)
            LearningLayer(
                label = "Staff note",
                body = stage.staffNote,
                accentColor = MaterialTheme.colorScheme.secondary,
            )
            SourceIds(sourceIds = stage.sourceIds, onViewSources = onViewSources)
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
