package com.ricven.richinsights.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val RichInsightsLightColorScheme = lightColorScheme(
    primary = RichInsightsBlue,
    onPrimary = RichInsightsSurface,
    primaryContainer = RichInsightsDeepNavy,
    onPrimaryContainer = RichInsightsSurface,
    secondary = RichInsightsCyan,
    onSecondary = RichInsightsPrimaryText,
    secondaryContainer = RichInsightsBackground,
    onSecondaryContainer = RichInsightsPrimaryText,
    tertiary = RichInsightsGold,
    onTertiary = RichInsightsPrimaryText,
    background = RichInsightsBackground,
    onBackground = RichInsightsPrimaryText,
    surface = RichInsightsSurface,
    onSurface = RichInsightsPrimaryText,
    surfaceVariant = RichInsightsBackground,
    onSurfaceVariant = RichInsightsSecondaryText,
    error = RichInsightsError,
    onError = RichInsightsSurface,
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
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = RichInsightsLightColorScheme,
        typography = RichInsightsTypography,
        shapes = RichInsightsShapes,
        content = content,
    )
}
