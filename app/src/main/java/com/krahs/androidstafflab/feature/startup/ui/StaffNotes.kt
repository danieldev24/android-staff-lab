package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.krahs.androidstafflab.feature.startup.content.StaffNote
import com.krahs.androidstafflab.ui.designsystem.LabOrganicPanel
import com.krahs.androidstafflab.ui.designsystem.LabSectionHeader

@Composable
fun StaffNotes(
    notes: List<StaffNote>,
    onViewSources: (List<String>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedIndex by rememberSaveable { mutableIntStateOf(0) }
    val selectedNote = notes[selectedIndex]

    LabOrganicPanel(modifier = modifier.fillMaxWidth()) {
        LabSectionHeader(
            title = "Staff notes & caveats",
            supportingLabel = "${selectedIndex + 1} / ${notes.size}",
        )
        Text(
            modifier = Modifier.padding(top = 12.dp),
            text = "Select one boundary at a time to keep the mental model precise.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            notes.forEachIndexed { index, note ->
                FilterChip(
                    selected = selectedIndex == index,
                    onClick = { selectedIndex = index },
                    label = { Text((index + 1).toString().padStart(2, '0')) },
                    modifier = Modifier
                        .sizeIn(minHeight = 48.dp)
                        .testTag("staff-note-selector-${note.id}"),
                )
            }
        }
        StaffNoteCard(
            index = selectedIndex,
            note = selectedNote,
            onViewSources = { onViewSources(selectedNote.sourceIds) },
            modifier = Modifier.padding(top = 12.dp),
        )
    }
}

@Composable
private fun StaffNoteCard(
    index: Int,
    note: StaffNote,
    onViewSources: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .testTag("staff-note"),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 2.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = "${(index + 1).toString().padStart(2, '0')} / STAFF BOUNDARY",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelSmall,
            )
            Text(
                modifier = Modifier.semantics { heading() },
                text = note.title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = note.body,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
            TextButton(
                onClick = onViewSources,
                modifier = Modifier.testTag("staff-note-${note.id}-sources"),
            ) {
                Text("View official sources · ${note.sourceIds.size}")
            }
        }
    }
}
