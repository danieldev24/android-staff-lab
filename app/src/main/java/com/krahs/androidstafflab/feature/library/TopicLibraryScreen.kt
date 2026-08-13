package com.krahs.androidstafflab.feature.library

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krahs.androidstafflab.ui.designsystem.LabIconBadge
import com.krahs.androidstafflab.ui.designsystem.LabPill
import com.krahs.androidstafflab.ui.theme.AndroidStaffLabTheme

@Composable
fun TopicLibraryScreen(
    category: TopicCategory,
    onTopicClick: (Topic) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 760.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
        ) {
            LibraryHeader()
            LibraryIntroduction()
            CategorySection(category = category, onTopicClick = onTopicClick)
            Text(
                text = "Next categories: App Architecture, UI Runtime, Data & Connectivity, and Performance.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun LibraryHeader() {
    val usesLargeText = LocalDensity.current.fontScale >= 1.5f

    if (usesLargeText) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LibraryMark()
                Text(
                    modifier = Modifier.weight(1f),
                    text = "Android Staff Lab",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Text(
                text = "Learn the system, not just the API",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            LabPill(label = "LEVEL · STAFF")
        }
        return
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LibraryMark()
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Android Staff Lab", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "Learn the system, not just the API",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        LabPill(label = "LEVEL · STAFF")
    }
}

@Composable
private fun LibraryMark() {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = CircleShape,
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "AS",
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun LibraryIntroduction() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        LabPill(label = "STAFF LEARNING PATH")
        Text(
            modifier = Modifier.semantics { heading() },
            text = "Master Android from the inside out",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Short visual lessons that connect platform internals, runtime evidence, and staff-level trade-offs.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun CategorySection(
    category: TopicCategory,
    onTopicClick: (Topic) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("topic-category-${category.id}"),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                LabIconBadge(
                    symbol = category.symbol,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                )
                LabPill(
                    label = "${category.topics.size} ${if (category.topics.size == 1) "TOPIC" else "TOPICS"}",
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.primary,
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = "CATEGORY ${category.sequence}",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = category.title,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = category.summary,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            Text(
                text = "START HERE",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
            )
            category.topics.forEach { topic ->
                TopicCard(topic = topic, onClick = { onTopicClick(topic) })
            }
        }
    }
}

@Composable
private fun TopicCard(
    topic: Topic,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val accentColor = MaterialTheme.colorScheme.primary
    val usesLargeText = LocalDensity.current.fontScale >= 1.5f

    Surface(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .testTag("topic-card-${topic.id}"),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        shadowElevation = 4.dp,
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawRect(color = accentColor)
                }
            }
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (usesLargeText) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LabIconBadge(
                            symbol = topic.sequence,
                            color = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                        )
                        LabPill(
                            label = "READY",
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Entry ${topic.sequence} · ${topic.title}",
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        LabIconBadge(
                            symbol = topic.sequence,
                            color = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary,
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Entry ${topic.sequence} · ${topic.title}",
                                style = MaterialTheme.typography.labelSmall,
                            )
                        }
                        LabPill(
                            label = "READY",
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                        )
                    }
                }
                Text(
                    modifier = Modifier.semantics { heading() },
                    text = topic.question,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = topic.summary,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    LabPill(label = "4 LESSONS")
                    LabPill(label = "9 STAGES")
                    LabPill(label = "INTERACTIVE")
                }
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                        text = "START LESSON →",
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopicLibraryScreenPreview() {
    AndroidStaffLabTheme {
        TopicLibraryScreen(category = TopicCategories.androidPlatform, onTopicClick = {})
    }
}
