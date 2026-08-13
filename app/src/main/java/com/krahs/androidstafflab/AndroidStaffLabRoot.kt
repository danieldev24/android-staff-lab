package com.krahs.androidstafflab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krahs.androidstafflab.ui.theme.AndroidStaffLabTheme
import com.krahs.androidstafflab.ui.theme.traceColors

@Composable
fun AndroidStaffLabRoot(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp),
        ) {
            LabHeader()
            Spacer(modifier = Modifier.height(56.dp))
            Text(
                text = "FIELD NOTE 001  ·  APPLICATION STARTUP",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.labelLarge,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Trace the launch.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.displaySmall,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "What happens when an Android application starts?",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Theo dõi critical path từ launch request đến first frame — qua system_server, Zygote, app process và Compose.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(32.dp))
            TracePreview()
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Foundation ready · Topic library comes next",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

@Composable
private fun LabHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primary),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "AS",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Black,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
            Column {
                Text(
                    text = "ANDROID STAFF LAB",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "SYSTEMS · RUNTIME · PERFORMANCE",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
        Text(
            text = "01",
            color = MaterialTheme.colorScheme.outline,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
private fun TracePreview() {
    val traceColors = MaterialTheme.traceColors

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "PROCESS TRACE / PREVIEW",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
            )
            TraceLane(color = traceColors.user, label = "USER", event = "Tap launcher")
            TraceLane(color = traceColors.system, label = "SYSTEM", event = "Resolve · schedule")
            TraceLane(color = traceColors.runtime, label = "RUNTIME", event = "Fork · specialize")
            TraceLane(color = traceColors.app, label = "APP", event = "Create · compose")
            TraceLane(color = traceColors.render, label = "RENDER", event = "First frame")
        }
    }
}

@Composable
private fun TraceLane(
    color: Color,
    label: String,
    event: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(RoundedCornerShape(50))
                .background(color),
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                color = color,
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = event,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AndroidStaffLabRootPreview() {
    AndroidStaffLabTheme {
        AndroidStaffLabRoot()
    }
}

@Preview(showBackground = true)
@Composable
private fun AndroidStaffLabRootDarkPreview() {
    AndroidStaffLabTheme(darkTheme = true) {
        AndroidStaffLabRoot()
    }
}

