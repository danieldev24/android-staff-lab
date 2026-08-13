package com.krahs.androidstafflab.feature.startup.ui

import android.animation.ValueAnimator
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
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
import com.krahs.androidstafflab.feature.startup.model.StartupMode
import com.krahs.androidstafflab.feature.startup.model.StartupSimulationAction
import com.krahs.androidstafflab.feature.startup.model.StartupSimulationState
import com.krahs.androidstafflab.startuptrace.StartupEventRecord
import com.krahs.androidstafflab.startuptrace.StartupEventRecorder
import com.krahs.androidstafflab.ui.designsystem.LabPill
import com.krahs.androidstafflab.ui.theme.AndroidStaffLabTheme
import kotlinx.coroutines.delay

@Immutable
private enum class StartupLesson(
    val label: String,
    val eyebrow: String,
    val title: String,
    val summary: String,
) {
    OVERVIEW(
        label = "Overview",
        eyebrow = "ORIENT",
        title = "Build the mental model",
        summary = "Start with the boundaries, then trace, experiment, and validate against a real run.",
    ),
    FLOW(
        label = "Flow",
        eyebrow = "TRACE",
        title = "Follow one launch",
        summary = "Move across five execution lanes and inspect one stage at a time.",
    ),
    LAB(
        label = "Lab",
        eyebrow = "EXPERIMENT",
        title = "Shape the critical path",
        summary = "Toggle deterministic workloads and connect main-thread work to TTID and TTFD.",
    ),
    EVIDENCE(
        label = "Evidence",
        eyebrow = "VALIDATE",
        title = "Compare model with reality",
        summary = "Read this process launch, then review the caveats that keep the model honest.",
    ),
}

private enum class EvidenceView(val label: String) {
    LIVE_TRACE("Live trace"),
    STAFF_NOTES("Staff notes"),
}

@Composable
fun ApplicationStartupScreen(
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: StartupFlowViewModel = viewModel(),
) {
    val stages = StartupContent.coldStartStages
    val startupEvents = remember { StartupEventRecorder.snapshot() }
    val flowState by viewModel.uiState.collectAsStateWithLifecycle()
    val simulationState by viewModel.simulationState.collectAsStateWithLifecycle()
    val reducedMotion = !ValueAnimator.areAnimatorsEnabled()

    LaunchedEffect(
        flowState.isPlaying,
        flowState.currentStageId,
        flowState.mode,
        reducedMotion,
    ) {
        if (flowState.isPlaying) {
            if (reducedMotion) {
                viewModel.onAction(StartupFlowAction.PlayPause)
            } else {
                delay(1_200)
                viewModel.onAction(StartupFlowAction.PlaybackTick)
            }
        }
    }

    ApplicationStartupContent(
        lanes = StartupLane.entries,
        stages = stages,
        flowState = flowState,
        onFlowAction = viewModel::onAction,
        simulationState = simulationState,
        onSimulationAction = viewModel::onSimulationAction,
        startupEvents = startupEvents,
        reducedMotion = reducedMotion,
        onBack = onBack,
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
    startupEvents: List<StartupEventRecord>,
    reducedMotion: Boolean = false,
    onBack: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val lessons = StartupLesson.entries
    val uriHandler = LocalUriHandler.current
    var currentLessonIndex by rememberSaveable { mutableIntStateOf(0) }
    var evidenceView by rememberSaveable { mutableStateOf(EvidenceView.LIVE_TRACE) }
    var lessonComplete by rememberSaveable { mutableStateOf(false) }
    var selectedSourceIds by remember { mutableStateOf(emptyList<String>()) }
    val currentLesson = lessons[currentLessonIndex]
    val selectedSources = selectedSourceIds.mapNotNull(StartupContent.sources::get)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            LessonProgressHeader(
                lessons = lessons,
                currentLessonIndex = currentLessonIndex,
                onLessonSelected = { index ->
                    currentLessonIndex = index
                    lessonComplete = false
                },
            )
        },
        bottomBar = {
            LessonNavigationBar(
                currentLessonIndex = currentLessonIndex,
                lessonCount = lessons.size,
                lessonComplete = lessonComplete,
                onBack = {
                    if (currentLessonIndex == 0) onBack()
                    else currentLessonIndex -= 1
                },
                onNext = {
                    when {
                        lessonComplete -> {
                            currentLessonIndex = 0
                            lessonComplete = false
                        }

                        currentLessonIndex < lessons.lastIndex -> currentLessonIndex += 1
                        else -> lessonComplete = true
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 760.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.TopCenter)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {
                LessonHeading(lesson = currentLesson)
                when (currentLesson) {
                    StartupLesson.OVERVIEW -> OverviewLesson()
                    StartupLesson.FLOW -> FlowLesson(
                        lanes = lanes,
                        stages = stages,
                        flowState = flowState,
                        onFlowAction = onFlowAction,
                        reducedMotion = reducedMotion,
                        onViewSources = { selectedSourceIds = it },
                    )

                    StartupLesson.LAB -> StartupLab(
                        state = simulationState,
                        onAction = onSimulationAction,
                    )

                    StartupLesson.EVIDENCE -> EvidenceLesson(
                        startupEvents = startupEvents,
                        evidenceView = evidenceView,
                        onEvidenceViewSelected = { evidenceView = it },
                        lessonComplete = lessonComplete,
                        onViewSources = { selectedSourceIds = it },
                    )
                }
            }
        }
    }

    if (selectedSources.isNotEmpty()) {
        SourceLinksSheet(
            sources = selectedSources,
            onDismiss = { selectedSourceIds = emptyList() },
            onOpenSource = { source -> uriHandler.openUri(source.url) },
        )
    }
}

@Composable
private fun LessonProgressHeader(
    lessons: List<StartupLesson>,
    currentLessonIndex: Int,
    onLessonSelected: (Int) -> Unit,
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 3.dp,
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text(
                        text = "Android Staff Lab",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                    )
                    Text(
                        text = "Application startup",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
                LabPill(label = "${currentLessonIndex + 1} / ${lessons.size}")
            }
            LinearProgressIndicator(
                progress = { (currentLessonIndex + 1f) / lessons.size },
                modifier = Modifier.fillMaxWidth(),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                lessons.forEachIndexed { index, lesson ->
                    FilterChip(
                        selected = currentLessonIndex == index,
                        onClick = { onLessonSelected(index) },
                        label = { Text(lesson.label) },
                        modifier = Modifier
                            .sizeIn(minHeight = 48.dp)
                            .testTag("startup-lesson-${lesson.name.lowercase()}"),
                    )
                }
            }
        }
    }
}

@Composable
private fun LessonHeading(lesson: StartupLesson) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = lesson.eyebrow,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge,
        )
        Text(
            modifier = Modifier.semantics { heading() },
            text = lesson.title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = lesson.summary,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun OverviewLesson() {
    StartupHero()
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
            Text(
                modifier = Modifier.semantics { heading() },
                text = "What you’ll learn",
                style = MaterialTheme.typography.titleLarge,
            )
            listOf(
                "Distinguish Cold, Warm, and Hot start",
                "Trace work across five execution lanes",
                "Connect main-thread work to TTID and TTFD",
                "Validate the model with a seven-hook live trace",
            ).forEachIndexed { index, outcome ->
                LearningOutcome(index = index + 1, text = outcome)
            }
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        LabPill(label = "4 SHORT LESSONS")
        LabPill(
            label = "9 STARTUP STAGES",
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        )
        LabPill(
            label = "SOURCE-BACKED",
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}

@Composable
private fun LearningOutcome(index: Int, text: String) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            modifier = Modifier.size(32.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(text = index.toString(), style = MaterialTheme.typography.labelLarge)
            }
        }
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun FlowLesson(
    lanes: List<StartupLane>,
    stages: List<StartupStage>,
    flowState: StartupFlowUiState,
    onFlowAction: (StartupFlowAction) -> Unit,
    reducedMotion: Boolean,
    onViewSources: (List<String>) -> Unit,
) {
    StartupFlowPlayer(
        state = flowState,
        stages = stages,
        onAction = onFlowAction,
        reducedMotion = reducedMotion,
    )
    StartupTimeline(
        lanes = lanes,
        stages = stages,
        selectedStageId = flowState.currentStageId,
        skippedStageIds = stages
            .map(StartupStage::id)
            .filterNot(flowState.activeStageIds::contains)
            .toSet(),
        modeLabel = flowState.mode.label,
        onStageSelected = { onFlowAction(StartupFlowAction.SelectStage(it)) },
        onViewSources = onViewSources,
    )
}

@Composable
private fun EvidenceLesson(
    startupEvents: List<StartupEventRecord>,
    evidenceView: EvidenceView,
    onEvidenceViewSelected: (EvidenceView) -> Unit,
    lessonComplete: Boolean,
    onViewSources: (List<String>) -> Unit,
) {
    if (lessonComplete) {
        LessonCompleteCard()
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        EvidenceView.entries.forEach { view ->
            FilterChip(
                selected = evidenceView == view,
                onClick = { onEvidenceViewSelected(view) },
                label = { Text(view.label) },
                modifier = Modifier
                    .weight(1f)
                    .sizeIn(minHeight = 48.dp)
                    .testTag("startup-evidence-${view.name.lowercase().replace('_', '-')}"),
            )
        }
    }
    when (evidenceView) {
        EvidenceView.LIVE_TRACE -> StartupEventLog(records = startupEvents)
        EvidenceView.STAFF_NOTES -> StaffNotes(
            notes = StartupContent.staffNotes,
            onViewSources = onViewSources,
        )
    }
}

@Composable
private fun LessonCompleteCard() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("startup-lesson-complete"),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = "✓ Lesson complete", style = MaterialTheme.typography.titleLarge)
            Text(
                text = "You can now explain where startup work runs, what blocks first frame, and where the model has platform boundaries.",
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
private fun LessonNavigationBar(
    currentLessonIndex: Int,
    lessonCount: Int,
    lessonComplete: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    val useLargeTextLayout = LocalDensity.current.fontScale >= 1.5f
    val backLabel = if (currentLessonIndex == 0) "Library" else "Previous"
    val nextLabel = when {
        lessonComplete -> "Review from start"
        currentLessonIndex == 0 -> "Start flow"
        currentLessonIndex < lessonCount - 1 -> "Next lesson"
        else -> "Finish lesson"
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp,
    ) {
        if (useLargeTextLayout) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = 48.dp)
                        .testTag("startup-lesson-next"),
                ) {
                    Text(nextLabel)
                }
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .sizeIn(minHeight = 48.dp)
                        .testTag("startup-lesson-previous"),
                ) {
                    Text(backLabel)
                }
            }
        } else {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 48.dp)
                        .testTag("startup-lesson-previous"),
                ) {
                    Text(backLabel)
                }
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .weight(1f)
                        .sizeIn(minHeight = 48.dp)
                        .testTag("startup-lesson-next"),
                ) {
                    Text(nextLabel)
                }
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
        shadowElevation = 6.dp,
    ) {
        Box {
            HeroTexture(modifier = Modifier.matchParentSize())
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                LabPill(
                    label = "FIELD NOTE 001",
                    containerColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.18f),
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = "What happens when an Android application starts?",
                    style = MaterialTheme.typography.headlineMedium,
                )
                Text(
                    text = "From launch request to a usable first frame — without treating implementation details as universal contracts.",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.82f),
                    style = MaterialTheme.typography.bodyLarge,
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
        ApplicationStartupPreviewContent()
    }
}

@Preview(name = "Large font", showBackground = true, fontScale = 2f)
@Composable
private fun ApplicationStartupScreenLargeFontPreview() {
    AndroidStaffLabTheme {
        ApplicationStartupPreviewContent()
    }
}

@Composable
private fun ApplicationStartupPreviewContent() {
    val stages = StartupContent.coldStartStages
    ApplicationStartupContent(
        lanes = StartupLane.entries,
        stages = stages,
        flowState = StartupFlowUiState(
            mode = StartupMode.COLD,
            currentStageId = stages.first().id,
            activeStageIds = stages.map(StartupStage::id).toSet(),
            isPlaying = false,
        ),
        onFlowAction = {},
        simulationState = StartupSimulationState(),
        onSimulationAction = {},
        startupEvents = emptyList(),
    )
}
