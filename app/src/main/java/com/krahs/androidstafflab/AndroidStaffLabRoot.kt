package com.krahs.androidstafflab

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.shape.CircleShape
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
import com.krahs.androidstafflab.ui.designsystem.LabHatchedBand
import com.krahs.androidstafflab.ui.designsystem.LabIconBadge
import com.krahs.androidstafflab.ui.designsystem.LabOrganicPanel
import com.krahs.androidstafflab.ui.designsystem.LabPill
import com.krahs.androidstafflab.ui.designsystem.LabSectionHeader
import com.krahs.androidstafflab.navigation.AppNavigation
import com.krahs.androidstafflab.ui.theme.AndroidStaffLabTheme
import com.krahs.androidstafflab.ui.theme.traceColors

@Composable
fun AndroidStaffLabRoot(modifier: Modifier = Modifier) {
    AppNavigation(modifier = modifier)
}

@Composable
internal fun ApplicationStartupScreen(modifier: Modifier = Modifier) {
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
            LabHeader()
            Spacer(modifier = Modifier.height(36.dp))
            LabHatchedBand(modifier = Modifier.padding(horizontal = 12.dp))
            StartupHero()
            Spacer(modifier = Modifier.height(32.dp))
            TracePreview()
            Spacer(modifier = Modifier.height(24.dp))
            LabPill(
                label = "FOUNDATION READY",
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun LabHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
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
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "01",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
            )
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
            center = androidx.compose.ui.geometry.Offset(size.width * 0.88f, size.height * 0.12f),
        )
        drawCircle(
            color = highlight.copy(alpha = 0.10f),
            radius = size.minDimension * 0.34f,
            center = androidx.compose.ui.geometry.Offset(size.width * 0.82f, size.height * 0.85f),
        )
    }
}

@Composable
private fun TracePreview() {
    val colors = MaterialTheme.traceColors

    LabOrganicPanel(modifier = Modifier.fillMaxWidth()) {
        LabSectionHeader(
            title = "Trace the launch",
            supportingLabel = "Preview",
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Theo dõi critical path từ launch request đến first frame — qua system_server, Zygote, app process và Compose.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(modifier = Modifier.height(22.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surface,
            shadowElevation = 4.dp,
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                TraceLane("01", colors.user, "USER", "Tap launcher")
                TraceLane("02", colors.system, "SYSTEM", "Resolve · schedule")
                TraceLane("03", colors.runtime, "RUNTIME", "Fork · specialize")
                TraceLane("04", colors.app, "APP", "Create · compose")
                TraceLane("05", colors.render, "RENDER", "First frame")
            }
        }
    }
}

@Composable
private fun TraceLane(
    order: String,
    color: Color,
    label: String,
    event: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LabIconBadge(symbol = order, color = color)
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

@Preview(
    name = "Large font",
    showBackground = true,
    fontScale = 2f,
)
@Composable
private fun AndroidStaffLabRootLargeFontPreview() {
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
