package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.krahs.androidstafflab.feature.startup.StartupFlowViewModel
import com.krahs.androidstafflab.feature.startup.content.StartupContent
import com.krahs.androidstafflab.feature.startup.content.StartupLane
import com.krahs.androidstafflab.feature.startup.content.StartupStage
import com.krahs.androidstafflab.feature.startup.model.StartupFlowAction
import com.krahs.androidstafflab.feature.startup.model.StartupFlowUiState
import com.krahs.androidstafflab.feature.startup.model.StartupSimulationAction
import com.krahs.androidstafflab.feature.startup.model.StartupSimulationState
import com.krahs.androidstafflab.ui.designsystem.LabHatchedBand
import com.krahs.androidstafflab.ui.designsystem.LabOrganicPanel
import com.krahs.androidstafflab.ui.designsystem.LabPill
import com.krahs.androidstafflab.ui.designsystem.LabSectionHeader
import com.krahs.androidstafflab.ui.theme.AndroidStaffLabTheme
import kotlinx.coroutines.delay

@Composable
fun ApplicationStartupScreen(
    modifier: Modifier = Modifier,
    viewModel: StartupFlowViewModel = viewModel(),
) {
    val stages = StartupContent.coldStartStages
    val flowState by viewModel.uiState.collectAsStateWithLifecycle()
    val simulationState by viewModel.simulationState.collectAsStateWithLifecycle()

    LaunchedEffect(
        flowState.isPlaying,
        flowState.currentStageId,
        flowState.mode,
    ) {
        if (flowState.isPlaying) {
            delay(1_200)
            viewModel.onAction(StartupFlowAction.PlaybackTick)
        }
    }

    ApplicationStartupContent(
        lanes = StartupLane.entries,
        stages = stages,
        flowState = flowState,
        onFlowAction = viewModel::onAction,
        simulationState = simulationState,
        onSimulationAction = viewModel::onSimulationAction,
        modifier = modifier,
    )
}

@Composable
private fun ApplicationStartupContent(
    lanes: List<StartupLane>,
    stages: List<StartupStage>,
    flowState: StartupFlowUiState,
    onFlowAction: (StartupFlowAction) -> Unit,
    simulationState: StartupSimulationState,
    onSimulationAction: (StartupSimulationAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 18.dp),
        ) {
            StartupHeader()
            Spacer(modifier = Modifier.height(36.dp))
            LabHatchedBand(modifier = Modifier.padding(horizontal = 12.dp))
            StartupHero()
            Spacer(modifier = Modifier.height(32.dp))
            LabOrganicPanel(modifier = Modifier.fillMaxWidth()) {
                LabSectionHeader(
                    title = "Cold-start timeline",
                    supportingLabel = "${stages.size} stages",
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Theo dõi critical path qua năm execution lane. Chọn một stage để mở What / Where / Why và staff note.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(22.dp))
                StartupFlowPlayer(
                    state = flowState,
                    stages = stages,
                    onAction = onFlowAction,
                )
                Spacer(modifier = Modifier.height(20.dp))
                StartupTimeline(
                    lanes = lanes,
                    stages = stages,
                    selectedStageId = flowState.currentStageId,
                    skippedStageIds = stages
                        .map(StartupStage::id)
                        .filterNot(flowState.activeStageIds::contains)
                        .toSet(),
                    modeLabel = flowState.mode.label,
                    onStageSelected = { stageId ->
                        onFlowAction(StartupFlowAction.SelectStage(stageId))
                    },
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            StartupLab(
                state = simulationState,
                onAction = onSimulationAction,
            )
            Spacer(modifier = Modifier.height(20.dp))
            LabPill(
                label = "${flowState.mode.label.uppercase()} MODE · SOURCE-BACKED",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun StartupHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "AS",
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Android Staff Lab",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Systems · Runtime · Performance",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
            )
        }
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.primary,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "01",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@Composable
private fun StartupHero() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shadowElevation = 8.dp,
    ) {
        Box {
            HeroTexture(modifier = Modifier.matchParentSize())
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                LabPill(
                    label = "FIELD NOTE 001",
                    containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = "Application\nstartup",
                    style = MaterialTheme.typography.displaySmall,
                )
                Text(
                    text = "What happens when an Android application starts?",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.88f),
                    style = MaterialTheme.typography.bodyLarge,
                )
                LabPill(
                    label = "5 LANES · COLD START",
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

@Composable
private fun HeroTexture(modifier: Modifier = Modifier) {
    val highlight = MaterialTheme.colorScheme.onPrimary
    Canvas(modifier = modifier) {
        drawCircle(
            color = highlight.copy(alpha = 0.08f),
            radius = size.minDimension * 0.48f,
            center = Offset(size.width * 0.88f, size.height * 0.12f),
        )
        drawCircle(
            color = highlight.copy(alpha = 0.10f),
            radius = size.minDimension * 0.34f,
            center = Offset(size.width * 0.82f, size.height * 0.85f),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ApplicationStartupScreenPreview() {
    AndroidStaffLabTheme {
        ApplicationStartupScreen()
    }
}

@Preview(
    name = "Large font",
    showBackground = true,
    fontScale = 2f,
)
@Composable
private fun ApplicationStartupScreenLargeFontPreview() {
    AndroidStaffLabTheme {
        ApplicationStartupScreen()
    }
}
