package com.ricven.richinsights.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val RichInsightsLightColorScheme = lightColorScheme(
    primary = RichInsightsBlue,
    onPrimary = RichInsightsSurface,
    primaryContainer = RichInsightsSurfaceStrong,
    onPrimaryContainer = RichInsightsOnStrongSurface,
    secondary = RichInsightsCyan,
    onSecondary = RichInsightsPrimaryText,
    secondaryContainer = RichInsightsSurfaceAccent,
    onSecondaryContainer = RichInsightsPrimaryText,
    tertiary = RichInsightsGold,
    onTertiary = RichInsightsPrimaryText,
    background = RichInsightsSurface,
    onBackground = RichInsightsPrimaryText,
    surface = RichInsightsSurface,
    onSurface = RichInsightsPrimaryText,
    surfaceVariant = RichInsightsSurfaceAccent,
    onSurfaceVariant = RichInsightsSecondaryText,
    surfaceTint = RichInsightsBlue,
    error = RichInsightsError,
    onError = RichInsightsSurface,
)

private val RichInsightsDarkColorScheme = darkColorScheme(
    primary = RichInsightsCyan,
    onPrimary = RichInsightsDeepNavy,
    primaryContainer = RichInsightsDarkSurfaceStrong,
    onPrimaryContainer = RichInsightsDarkPrimaryText,
    secondary = Color(0xFF66D1FF),
    onSecondary = RichInsightsDeepNavy,
    secondaryContainer = RichInsightsDarkSurfaceAccent,
    onSecondaryContainer = RichInsightsDarkPrimaryText,
    tertiary = RichInsightsGold,
    onTertiary = RichInsightsDeepNavy,
    background = RichInsightsDarkBackground,
    onBackground = RichInsightsDarkPrimaryText,
    surface = RichInsightsDarkSurface,
    onSurface = RichInsightsDarkPrimaryText,
    surfaceVariant = RichInsightsDarkSurfaceAccent,
    onSurfaceVariant = RichInsightsDarkSecondaryText,
    surfaceTint = RichInsightsCyan,
    error = RichInsightsDarkError,
    onError = RichInsightsDeepNavy,
)

private val RichInsightsShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

object RichInsightsSpacing {
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 32.dp

    val smallMedium = 12.dp
    val extraLargePlus = 40.dp
}

@Composable
fun RichInsightsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkTheme) RichInsightsDarkColorScheme else RichInsightsLightColorScheme,
        typography = RichInsightsTypography,
        shapes = RichInsightsShapes,
        content = content,
    )
}
