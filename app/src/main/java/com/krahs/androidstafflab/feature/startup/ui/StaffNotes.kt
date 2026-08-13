package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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
    LabOrganicPanel(modifier = modifier.fillMaxWidth()) {
        LabSectionHeader(
            title = "Staff notes & caveats",
            supportingLabel = "${notes.size} boundaries",
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Các boundary dưới đây giữ mental model chính xác khi platform, entry point hoặc readiness definition thay đổi.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(18.dp))
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            notes.forEachIndexed { index, note ->
                StaffNoteCard(
                    index = index,
                    note = note,
                    onViewSources = { onViewSources(note.sourceIds) },
                )
            }
        }
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "${(index + 1).toString().padStart(2, '0')} / STAFF",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelSmall,
                )
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .semantics { heading() },
                    text = note.title,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
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
