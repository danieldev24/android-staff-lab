package com.krahs.androidstafflab.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

@Immutable
data class TraceColors(
    val user: Color,
    val system: Color,
    val runtime: Color,
    val app: Color,
    val render: Color,
    val critical: Color,
)

private val LocalTraceColors = staticCompositionLocalOf { LightTraceColors }

val MaterialTheme.traceColors: TraceColors
    @Composable
    @ReadOnlyComposable
    get() = LocalTraceColors.current

private val LightColorScheme = lightColorScheme(
    primary = Violet,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE9E4FF),
    onPrimaryContainer = Color(0xFF21105E),
    secondary = Coral,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFE1DD),
    onSecondaryContainer = Color(0xFF4A0804),
    tertiary = Color(0xFF008B73),
    onTertiary = Color.White,
    background = Canvas,
    onBackground = Ink,
    surface = White,
    onSurface = Ink,
    surfaceVariant = Lavender,
    onSurfaceVariant = Muted,
    outline = Hairline,
    error = Color(0xFFB3261E),
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFC9B9FF),
    onPrimary = Color(0xFF2D126F),
    primaryContainer = Color(0xFF49319C),
    onPrimaryContainer = Color(0xFFE9E0FF),
    secondary = Color(0xFFFFB4AB),
    onSecondary = Color(0xFF690005),
    secondaryContainer = Color(0xFF8C1D18),
    onSecondaryContainer = Color(0xFFFFDAD0),
    tertiary = Color(0xFF70E4C8),
    onTertiary = Color(0xFF00382D),
    background = Night,
    onBackground = Color(0xFFE2E9ED),
    surface = NightSurface,
    onSurface = Color(0xFFE2E9ED),
    surfaceVariant = NightLavender,
    onSurfaceVariant = Color(0xFFC9C5D0),
    outline = Color(0xFF918D99),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
)

private val AndroidStaffShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(30.dp),
)

@Composable
fun AndroidStaffLabTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    androidx.compose.runtime.CompositionLocalProvider(
        LocalTraceColors provides if (darkTheme) DarkTraceColors else LightTraceColors,
    ) {
        MaterialTheme(
            colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
            typography = AndroidStaffTypography,
            shapes = AndroidStaffShapes,
            content = content,
        )
    }
}
