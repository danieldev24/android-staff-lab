package com.krahs.androidstafflab.ui.designsystem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class ConcaveTopShape(
    private val cornerRadius: Dp = 28.dp,
    private val notchWidth: Dp = 104.dp,
    private val notchDepth: Dp = 24.dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val radius = with(density) { cornerRadius.toPx() }
            .coerceAtMost(minOf(size.width, size.height) / 2f)
        val width = with(density) { notchWidth.toPx() }.coerceAtMost(size.width / 2f)
        val depth = with(density) { notchDepth.toPx() }.coerceAtMost(size.height / 3f)
        val center = size.width / 2f
        val path = Path().apply {
            moveTo(radius, 0f)
            lineTo(center - width / 2f, 0f)
            cubicTo(
                center - width / 4f,
                0f,
                center - width / 4f,
                depth,
                center,
                depth,
            )
            cubicTo(
                center + width / 4f,
                depth,
                center + width / 4f,
                0f,
                center + width / 2f,
                0f,
            )
            lineTo(size.width - radius, 0f)
            quadraticTo(size.width, 0f, size.width, radius)
            lineTo(size.width, size.height - radius)
            quadraticTo(size.width, size.height, size.width - radius, size.height)
            lineTo(radius, size.height)
            quadraticTo(0f, size.height, 0f, size.height - radius)
            lineTo(0f, radius)
            quadraticTo(0f, 0f, radius, 0f)
            close()
        }
        return Outline.Generic(path)
    }
}

@Composable
fun LabPill(
    label: String,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Surface(
        modifier = modifier,
        shape = CircleShape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            text = label,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun LabIconBadge(
    symbol: String,
    color: Color,
    modifier: Modifier = Modifier,
    contentColor: Color = MaterialTheme.colorScheme.surface,
) {
    Box(
        modifier = modifier
            .size(48.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(color = color)
            drawCircle(
                color = contentColor.copy(alpha = 0.35f),
                radius = size.minDimension / 2f - 3.dp.toPx(),
                style = Stroke(width = 1.dp.toPx()),
            )
        }
        Text(
            text = symbol,
            color = contentColor,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}

@Composable
fun LabHatchedBand(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.outline.copy(alpha = 0.42f),
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(18.dp),
    ) {
        drawHatching(color)
    }
}

private fun DrawScope.drawHatching(color: Color) {
    val gap = 18.dp.toPx()
    val stroke = 5.dp.toPx()
    var x = -size.height
    while (x < size.width + size.height) {
        translate(left = x) {
            drawLine(
                color = color,
                start = androidx.compose.ui.geometry.Offset(0f, size.height),
                end = androidx.compose.ui.geometry.Offset(size.height, 0f),
                strokeWidth = stroke,
            )
        }
        x += gap
    }
}

@Composable
fun LabOrganicPanel(
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        shape = ConcaveTopShape(),
        color = containerColor,
    ) {
        Column(
            modifier = Modifier.padding(start = 20.dp, top = 46.dp, end = 20.dp, bottom = 24.dp),
            content = content,
        )
    }
}

@Composable
fun LabSectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    supportingLabel: String? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
        )
        if (supportingLabel != null) {
            Text(
                text = supportingLabel,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
