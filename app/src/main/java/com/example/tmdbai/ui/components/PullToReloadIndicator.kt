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
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PullToReloadIndicator(
    state: PullRefreshState,
    modifier: Modifier = Modifier,
    color: Color = Color.Red,
    size: androidx.compose.ui.unit.Dp = 144.dp // Triple-sized (48dp * 3)
) {
    val animatedProgress by animateFloatAsState(
        targetValue = state.progress.coerceIn(0f, 1f),
        animationSpec = tween(100),
        label = "pull_progress"
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
    size: androidx.compose.ui.unit.Dp = 144.dp
) {
    Canvas(
        modifier = modifier.size(size)
    ) {
        drawPullToReloadArrow(
            progress = 1f, // Always show full arrow
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
    val centerX = size / 2f
    val centerY = size / 2f
    val arrowSize = size * 0.3f
    val strokeWidth = size * 0.08f

    // Draw the main arrow body (vertical line)
    val arrowBodyStart = centerY - arrowSize * 0.6f
    val arrowBodyEnd = centerY + arrowSize * 0.6f

    drawLine(
        color = color,
        start = Offset(centerX, arrowBodyStart),
        end = Offset(centerX, arrowBodyEnd),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round
    )

    // Draw arrow head (pointing down)
    val arrowHeadSize = arrowSize * 0.4f
    val path = Path().apply {
        moveTo(centerX, arrowBodyEnd)
        lineTo(centerX - arrowHeadSize * 0.5f, arrowBodyEnd - arrowHeadSize)
        moveTo(centerX, arrowBodyEnd)
        lineTo(centerX + arrowHeadSize * 0.5f, arrowBodyEnd - arrowHeadSize)
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
    if (progress > 0.1f) {
        val pullLinesAlpha = (progress - 0.1f) / 0.9f
        val pullLinesColor = color.copy(alpha = pullLinesAlpha)

        // Draw curved lines indicating pull direction
        val curveRadius = arrowSize * 0.8f
        val curveStart = centerY - arrowSize * 0.8f
        val curveEnd = centerY - arrowSize * 0.2f

        // Left curve
        val leftPath = Path().apply {
            moveTo(centerX - curveRadius * 0.3f, curveStart)
            cubicTo(
                centerX - curveRadius * 0.4f, curveStart,
                centerX - curveRadius * 0.5f, (curveStart + curveEnd) / 2f,
                centerX - curveRadius * 0.2f, curveEnd
            )
        }

        // Right curve
        val rightPath = Path().apply {
            moveTo(centerX + curveRadius * 0.3f, curveStart)
            cubicTo(
                centerX + curveRadius * 0.4f, curveStart,
                centerX + curveRadius * 0.5f, (curveStart + curveEnd) / 2f,
                centerX + curveRadius * 0.2f, curveEnd
            )
        }

        drawPath(
            path = leftPath,
            color = pullLinesColor,
            style = Stroke(
                width = strokeWidth * 0.7f,
                cap = StrokeCap.Round
            )
        )

        drawPath(
            path = rightPath,
            color = pullLinesColor,
            style = Stroke(
                width = strokeWidth * 0.7f,
                cap = StrokeCap.Round
            )
        )
    }
}
