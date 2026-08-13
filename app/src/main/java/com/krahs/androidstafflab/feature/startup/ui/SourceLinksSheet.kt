package com.krahs.androidstafflab.feature.startup.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.krahs.androidstafflab.feature.startup.content.StartupSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SourceLinksSheet(
    sources: List<StartupSource>,
    onDismiss: () -> Unit,
    onOpenSource: (StartupSource) -> Unit,
    modifier: Modifier = Modifier,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
        ) {
            Text(
                modifier = Modifier.semantics { heading() },
                text = "Official sources",
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Open the exact Android or AOSP page behind this claim.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                sources.forEach { source ->
                    OutlinedButton(
                        onClick = { onOpenSource(source) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("source-link-${source.id}"),
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(2.dp),
                        ) {
                            Text(
                                text = source.id,
                                style = MaterialTheme.typography.labelSmall,
                            )
                            Text(
                                text = source.title,
                                style = MaterialTheme.typography.bodyLarge,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
