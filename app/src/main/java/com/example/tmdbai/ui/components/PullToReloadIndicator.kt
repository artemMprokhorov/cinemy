package com.example.tmdbai.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.tmdbai.R
import com.example.tmdbai.ui.theme.Dimens140
import com.example.tmdbai.ui.theme.Float0
import com.example.tmdbai.ui.theme.Float008
import com.example.tmdbai.ui.theme.Float01
import com.example.tmdbai.ui.theme.Float02
import com.example.tmdbai.ui.theme.Float03
import com.example.tmdbai.ui.theme.Float04
import com.example.tmdbai.ui.theme.Float05
import com.example.tmdbai.ui.theme.Float06
import com.example.tmdbai.ui.theme.Float07
import com.example.tmdbai.ui.theme.Float08
import com.example.tmdbai.ui.theme.Float09
import com.example.tmdbai.ui.theme.Float10
import com.example.tmdbai.ui.theme.Float2

private const val TWEEN_DURATION_MS = 100

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullToReloadIndicator(
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    size: Dp = Dimens140 // Triple-sized (48dp * 3)
) {
    val animatedProgress by animateFloatAsState(
        targetValue = state.progress.coerceIn(Float0, Float10),
        animationSpec = tween(TWEEN_DURATION_MS),
        label = stringResource(R.string.pull_progress_animation)
    )

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.size(size)
        ) {
            drawPullToReloadArrow(
                progress = animatedProgress,
                color = color,
                size = size.toPx()
            )
        }
    }
}

@Composable
fun PullToReloadArrow(
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    size: Dp = Dimens140
) {
    Canvas(
        modifier = modifier.size(size)
    ) {
        drawPullToReloadArrow(
            progress = Float10, // Always show full arrow
            color = color,
            size = size.toPx()
        )
    }
}

private fun DrawScope.drawPullToReloadArrow(
    progress: Float,
    color: Color,
    size: Float
) {
    val centerX = size / Float2
    val centerY = size / Float2
    val arrowSize = size * Float03
    val strokeWidth = size * Float008

    // Draw the main arrow body (vertical line)
    val arrowBodyStart = centerY - arrowSize * Float06
    val arrowBodyEnd = centerY + arrowSize * Float06

    drawLine(
        color = color,
        start = Offset(centerX, arrowBodyStart),
        end = Offset(centerX, arrowBodyEnd),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )

    // Draw arrow head (pointing down)
    val arrowHeadSize = arrowSize * Float04
    val path = Path().apply {
        moveTo(centerX, arrowBodyEnd)
        lineTo(centerX - arrowHeadSize * Float05, arrowBodyEnd - arrowHeadSize)
        moveTo(centerX, arrowBodyEnd)
        lineTo(centerX + arrowHeadSize * Float05, arrowBodyEnd - arrowHeadSize)
    }

    drawPath(
        path = path,
        color = color,
        style = Stroke(
            width = strokeWidth,
            cap = StrokeCap.Round
        )
    )

    // Draw pull indicator lines (fade in as user pulls)
    if (progress > Float01) {
        val pullLinesAlpha = (progress - Float01) / Float09
        val pullLinesColor = color.copy(alpha = pullLinesAlpha)

        // Draw curved lines indicating pull direction
        val curveRadius = arrowSize * Float08
        val curveStart = centerY - arrowSize * Float08
        val curveEnd = centerY - arrowSize * Float02

        // Left curve
        val leftPath = Path().apply {
            moveTo(centerX - curveRadius * Float03, curveStart)
            cubicTo(
                centerX - curveRadius * Float04, curveStart,
                centerX - curveRadius * Float05, (curveStart + curveEnd) / Float2,
                centerX - curveRadius * Float02, curveEnd
            )
        }

        // Right curve
        val rightPath = Path().apply {
            moveTo(centerX + curveRadius * Float03, curveStart)
            cubicTo(
                centerX + curveRadius * Float04, curveStart,
                centerX + curveRadius * Float05, (curveStart + curveEnd) / Float2,
                centerX + curveRadius * Float02, curveEnd
            )
        }

        drawPath(
            path = leftPath,
            color = pullLinesColor,
            style = Stroke(
                width = strokeWidth * Float07,
                cap = StrokeCap.Round
            )
        )

        drawPath(
            path = rightPath,
            color = pullLinesColor,
            style = Stroke(
                width = strokeWidth * Float07,
                cap = StrokeCap.Round
            )
        )
    }
}
