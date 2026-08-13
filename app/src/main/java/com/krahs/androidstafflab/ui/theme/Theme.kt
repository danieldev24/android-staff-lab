package com.krahs.androidstafflab.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

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
    primary = Navy,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD5E8F5),
    onPrimaryContainer = Color(0xFF0A2B42),
    secondary = Rust,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDBD0),
    onSecondaryContainer = Color(0xFF3A0B00),
    tertiary = Pine,
    onTertiary = Color.White,
    background = Paper,
    onBackground = Ink,
    surface = PaperSurface,
    onSurface = Ink,
    surfaceVariant = Color(0xFFE2E6E8),
    onSurfaceVariant = Color(0xFF46515A),
    outline = Color(0xFF6E7980),
    error = Color(0xFFB3261E),
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Sky,
    onPrimary = Color(0xFF00344E),
    primaryContainer = Color(0xFF124C6B),
    onPrimaryContainer = Color(0xFFC9E8FA),
    secondary = Coral,
    onSecondary = Color(0xFF5E1606),
    secondaryContainer = Color(0xFF7B2D19),
    onSecondaryContainer = Color(0xFFFFDAD0),
    tertiary = Mint,
    onTertiary = Color(0xFF00382D),
    background = Night,
    onBackground = Color(0xFFE2E9ED),
    surface = NightSurface,
    onSurface = Color(0xFFE2E9ED),
    surfaceVariant = Color(0xFF26343E),
    onSurfaceVariant = Color(0xFFBCC8CF),
    outline = Color(0xFF89959C),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
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
            content = content,
        )
    }
}

