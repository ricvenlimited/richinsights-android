package com.ricven.richinsights.ui.splash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ricven.richinsights.R
import kotlinx.coroutines.delay

private val RoyalNavy = Color(0xFF0B2970)
private val SignatureCyan = Color(0xFF19B5FE)
private val AchievementGold = Color(0xFFF4B942)
private val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
)

/**
 * First-launch-only brand sequence. The mark remains fixed; the wordmark and
 * tagline fade in below it. Total sequence is about 1.0 s, followed by a 150 ms
 * fade into the already-composed app shell.
 */
@Composable
fun BrandIntroOverlay(
    showMark: Boolean = true,
    onSequenceCompleted: suspend () -> Unit,
    onFinished: () -> Unit,
) {
    val wordmarkAlpha = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val overlayAlpha = remember { Animatable(1f) }

    // The short brand sequence cannot be dismissed with Back.
    BackHandler(enabled = true) {}

    LaunchedEffect(Unit) {
        delay(250L)
        wordmarkAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(300, easing = LinearOutSlowInEasing),
        )
        taglineAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(300, easing = LinearOutSlowInEasing),
        )
        delay(150L)
        onSequenceCompleted()
        overlayAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(150, easing = LinearOutSlowInEasing),
        )
        onFinished()
    }

    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(RoyalNavy)
            .alpha(overlayAlpha.value),
    ) {
        if (showMark) {
            Image(
                painter = painterResource(R.drawable.ic_splash_mark),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(288.dp),
            )
        }

        // The wordmark's top edge sits 94 dp below the screen centre.
        RichInsightsWordmark(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 118.dp)
                .alpha(wordmarkAlpha.value),
        )

        Text(
            text = "Grow. Excel.",
            color = SignatureCyan,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 166.dp)
                .alpha(taglineAlpha.value),
        )
    }
}

@Composable
private fun RichInsightsWordmark(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    var firstDotX by remember { mutableFloatStateOf(Float.NaN) }
    var firstDotY by remember { mutableFloatStateOf(Float.NaN) }
    var firstPieceWidth by remember { mutableFloatStateOf(0f) }
    var middlePieceWidth by remember { mutableFloatStateOf(0f) }
    var secondDotOffsetX by remember { mutableFloatStateOf(Float.NaN) }
    var secondDotY by remember { mutableFloatStateOf(Float.NaN) }

    val wordmarkStyle = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        lineHeight = 48.sp,
        color = Color.White,
    )

    Box(
        modifier = modifier
            .wrapContentSize()
            .clearAndSetSemantics { contentDescription = "RichInsights" },
    ) {
        androidx.compose.foundation.layout.Row(
            modifier = Modifier
                .wrapContentSize()
                .drawWithContent {
                    drawContent()
                    val radius = with(density) { 4.75.dp.toPx() }
                    if (firstDotX.isFinite()) {
                        drawCircle(
                            color = AchievementGold,
                            radius = radius,
                            center = Offset(firstDotX, firstDotY),
                        )
                    }
                    if (secondDotOffsetX.isFinite()) {
                        drawCircle(
                            color = AchievementGold,
                            radius = radius,
                            center = Offset(secondDotOffsetX, secondDotY),
                        )
                    }
                },
        ) {
            BasicText(
                text = "Rıch",
                style = wordmarkStyle,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                onTextLayout = { layout ->
                    val bounds = layout.getBoundingBox(1)
                    firstDotX = (bounds.left + bounds.right) / 2f
                    firstDotY = bounds.top + bounds.height * 0.08f
                    firstPieceWidth = layout.size.width.toFloat()
                },
            )
            BasicText(
                text = "I",
                style = wordmarkStyle.copy(color = SignatureCyan),
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                onTextLayout = { layout ->
                    middlePieceWidth = layout.size.width.toFloat()
                },
            )
            BasicText(
                text = "nsıghts",
                style = wordmarkStyle,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Clip,
                onTextLayout = { layout ->
                    val bounds = layout.getBoundingBox(2)
                    secondDotOffsetX = firstPieceWidth + middlePieceWidth +
                        (bounds.left + bounds.right) / 2f
                    secondDotY = bounds.top + bounds.height * 0.08f
                },
            )
        }
    }
}
