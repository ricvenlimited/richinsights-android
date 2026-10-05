package com.ricven.richinsights.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.ricven.richinsights.R

private val RichInsightsFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
)

private fun TextStyle.richInsights(weight: FontWeight): TextStyle =
    copy(
        fontFamily = RichInsightsFontFamily,
        fontWeight = weight,
    )

private val MaterialTypography = Typography()

/**
 * App-wide typography for RichInsights.
 *
 * Font family, hierarchy, and weights live here so individual screens do not
 * need to define their own typography system. Material 3's default sizes and
 * line heights are preserved to keep accessibility scaling behavior intact.
 */
val RichInsightsTypography = MaterialTypography.copy(
    displayLarge = MaterialTypography.displayLarge.richInsights(FontWeight.Bold),
    displayMedium = MaterialTypography.displayMedium.richInsights(FontWeight.Bold),
    displaySmall = MaterialTypography.displaySmall.richInsights(FontWeight.Bold),
    headlineLarge = MaterialTypography.headlineLarge.richInsights(FontWeight.Bold),
    headlineMedium = MaterialTypography.headlineMedium.richInsights(FontWeight.Bold),
    headlineSmall = MaterialTypography.headlineSmall.richInsights(FontWeight.Bold),
    titleLarge = MaterialTypography.titleLarge.richInsights(FontWeight.SemiBold),
    titleMedium = MaterialTypography.titleMedium.richInsights(FontWeight.SemiBold),
    titleSmall = MaterialTypography.titleSmall.richInsights(FontWeight.SemiBold),
    bodyLarge = MaterialTypography.bodyLarge.richInsights(FontWeight.Normal),
    bodyMedium = MaterialTypography.bodyMedium.richInsights(FontWeight.Normal),
    bodySmall = MaterialTypography.bodySmall.richInsights(FontWeight.Normal),
    labelLarge = MaterialTypography.labelLarge.richInsights(FontWeight.SemiBold),
    labelMedium = MaterialTypography.labelMedium.richInsights(FontWeight.SemiBold),
    labelSmall = MaterialTypography.labelSmall.richInsights(FontWeight.SemiBold),
)
