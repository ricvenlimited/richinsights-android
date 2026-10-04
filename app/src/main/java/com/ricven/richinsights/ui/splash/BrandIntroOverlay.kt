package com.ricven.richinsights.ui.splash

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.matchParentSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.semantics
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
import kotlinx.coroutines.launch

private val DeepNavy = Color(0xFF102A43)
private val ElectricCyan = Color(0xFF19B5FE)
private val WarmGold = Color(0xFFF4B942)
private val PlusJakartaSans = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
)

@Composable
fun BrandIntroOverlay(
    onFinished: () -> Unit,
) {
    val markScale = remember { Animatable(1f) }
    val markOffsetY = remember { Animatable(0f) }
    val wordmarkAlpha = remember { Animatable(0f) }
    val taglineAlpha = remember { Animatable(0f) }
    val overlayAlpha = remember { Animatable(1f) }
    var completed by remember { mutableStateOf(false) }

    fun finishNow() {
        if (!completed) {
            completed = true
            onFinished()
        }
    }

    BackHandler(enabled = true) { finishNow() }

    LaunchedEffect(Unit) {
        delay(400)
        launch {
            markScale.animateTo(
                targetValue = 0.47f,
                animationSpec = tween(500, easing = FastOutSlowInEasing),
            )
        }
        launch {
            markOffsetY.animateTo(
                targetValue = -70f,
                animationSpec = tween(500, easing = FastOutSlowInEasing),
            )
        }
        launch {
            wordmarkAlpha.animateTo(
                targetValue = 1f,
                animationSpec = tween(500, easing = FastOutSlowInEasing),
            )
        }
        delay(500)
        taglineAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(400, easing = FastOutSlowInEasing),
        )
        overlayAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(200, easing = FastOutSlowInEasing),
        )
        finishNow()
    }

    val density = LocalDensity.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepNavy)
            .alpha(overlayAlpha.value)
            .pointerInput(Unit) {
                detectTapGestures { finishNow() }
            }
            .semantics {
                customActions = listOf(
                    CustomAccessibilityAction("Skip intro") {
                        finishNow()
                        true
                    },
                )
            },
    ) {
        Image(
            painter = painterResource(R.drawable.ic_splash_mark),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .size(288.dp)
                .graphicsLayer {
                    scaleX = markScale.value
                    scaleY = markScale.value
                    translationY = with(density) { markOffsetY.value.dp.toPx() }
                },
        )

        BoxWithConstraints(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 54.dp)
                .alpha(wordmarkAlpha.value),
        ) {
            var wordmarkWidthPx by remember { mutableFloatStateOf(0f) }
            val maxWordmarkWidthPx = with(density) { (maxWidth * 0.66f).toPx() }
            val horizontalScale = if (wordmarkWidthPx > 0f) {
                minOf(1f, maxWordmarkWidthPx / wordmarkWidthPx)
            } else {
                1f
            }

            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .graphicsLayer {
                        scaleX = horizontalScale
                        scaleY = horizontalScale
                    },
            ) {
                RichInsightsWordmark(
                    modifier = Modifier.onSizeChanged { size ->
                        wordmarkWidthPx = size.width.toFloat()
                    },
                )
            }
        }

        Text(
            text = "Grow. Excel.",
            color = ElectricCyan,
            fontFamily = PlusJakartaSans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 102.dp)
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
        Row(modifier = Modifier.wrapContentSize()) {
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
                style = wordmarkStyle.copy(color = ElectricCyan),
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

        Canvas(modifier = Modifier.matchParentSize()) {
            val radius = with(density) { 3.4.dp.toPx() }
            if (firstDotX.isFinite()) {
                drawCircle(
                    color = WarmGold,
                    radius = radius,
                    center = Offset(firstDotX, firstDotY),
                )
            }
            if (secondDotOffsetX.isFinite()) {
                drawCircle(
                    color = WarmGold,
                    radius = radius,
                    center = Offset(secondDotOffsetX, secondDotY),
                )
            }
        }
    }
}
