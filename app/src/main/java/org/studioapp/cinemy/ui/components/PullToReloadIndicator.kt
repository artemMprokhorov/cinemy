package org.studioapp.cinemy.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import org.studioapp.cinemy.ui.theme.Dimens140
import org.studioapp.cinemy.ui.theme.Float008
import org.studioapp.cinemy.ui.theme.Float01
import org.studioapp.cinemy.ui.theme.Float02
import org.studioapp.cinemy.ui.theme.Float03
import org.studioapp.cinemy.ui.theme.Float04
import org.studioapp.cinemy.ui.theme.Float05
import org.studioapp.cinemy.ui.theme.Float06
import org.studioapp.cinemy.ui.theme.Float07
import org.studioapp.cinemy.ui.theme.Float08
import org.studioapp.cinemy.ui.theme.Float09
import org.studioapp.cinemy.ui.theme.Float10
import org.studioapp.cinemy.ui.theme.Float2

// PullToReloadIndicator removed - was unused in codebase

/**
 * Composable that renders a pull-to-reload arrow indicator with curved pull lines.
 *
 * This component draws a vertical arrow pointing down with optional curved lines
 * that fade in based on the pull progress. The arrow is centered within the provided
 * size and uses the specified color for all drawing operations.
 *
 * @param modifier Modifier to be applied to the Canvas
 * @param color Color used for drawing the arrow and pull lines
 * @param size Size of the arrow indicator in density-independent pixels
 */
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
