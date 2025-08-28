package com.example.tmdbai.ui.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tmdbai.ui.theme.Alpha03
import com.example.tmdbai.ui.theme.Alpha06
import com.example.tmdbai.ui.theme.Dimens100
import com.example.tmdbai.ui.theme.Dimens12
import com.example.tmdbai.ui.theme.Dimens120
import com.example.tmdbai.ui.theme.Dimens16
import com.example.tmdbai.ui.theme.Dimens2
import com.example.tmdbai.ui.theme.Dimens20
import com.example.tmdbai.ui.theme.Dimens300
import com.example.tmdbai.ui.theme.Dimens32
import com.example.tmdbai.ui.theme.Dimens4
import com.example.tmdbai.ui.theme.Dimens40
import com.example.tmdbai.ui.theme.Dimens6
import com.example.tmdbai.ui.theme.Dimens8
import com.example.tmdbai.ui.theme.Dimens80
import com.example.tmdbai.ui.theme.DimensNegative10
import com.example.tmdbai.ui.theme.DimensNegative15
import com.example.tmdbai.ui.theme.DimensNegative20
import com.example.tmdbai.ui.theme.DimensNegative40
import com.example.tmdbai.ui.theme.DimensNegative5
import com.example.tmdbai.ui.theme.DimensNegative8
import com.example.tmdbai.ui.theme.FilmStripBackground
import com.example.tmdbai.ui.theme.FilmStripHole
import com.example.tmdbai.ui.theme.LoadingBackground
import com.example.tmdbai.ui.theme.LoadingBackgroundSecondary
import com.example.tmdbai.ui.theme.LoadingBlue
import com.example.tmdbai.ui.theme.LoadingOrange
import com.example.tmdbai.ui.theme.LoadingRed
import com.example.tmdbai.ui.theme.LoadingRedDark
import com.example.tmdbai.ui.theme.ReelCenter
import com.example.tmdbai.ui.theme.ReelDark
import com.example.tmdbai.ui.theme.ReelLight
import com.example.tmdbai.ui.theme.Typography18
import com.example.tmdbai.ui.theme.Dimens0
import com.example.tmdbai.ui.theme.Dimens2
import com.example.tmdbai.ui.theme.Dimens10
import com.example.tmdbai.ui.theme.Dimens20
import com.example.tmdbai.ui.theme.Dimens30
import com.example.tmdbai.ui.theme.Dimens35
import com.example.tmdbai.ui.theme.Dimens45
import com.example.tmdbai.ui.theme.Dimens50
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun MovieLoadingScreen(
    progress: Float = Alpha03,
    onLoadingComplete: () -> Unit = {}
) {
    LaunchedEffect(key1 = true) {
        kotlinx.coroutines.delay(5000) // 5 seconds
        onLoadingComplete()
    }
    val infiniteTransition = rememberInfiniteTransition(label = "loading")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "film_reel_rotation"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        LoadingBackground,
                        LoadingBackgroundSecondary
                    )
                )
            )
            .padding(Dimens32)
    ) {
        // Movie scene illustration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens300)
                .align(Alignment.Center)
                .offset(y = DimensNegative40)
        ) {
            // Film strip background
            Box(
                modifier = Modifier
                    .fillMaxWidth(Alpha06)
                    .height(Dimens40)
                    .align(Alignment.BottomCenter)
                    .background(
                        FilmStripBackground,
                        RoundedCornerShape(Dimens4)
                    )
            ) {
                // Film strip holes
                val HOLE_COUNT = 8
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(HOLE_COUNT) {
                        Box(
                            modifier = Modifier
                                .size(Dimens8)
                                .background(
                                    FilmStripHole,
                                    CircleShape
                                )
                        )
                    }
                }
            }

            // Film reel
            Box(
                modifier = Modifier
                    .size(Dimens120)
                    .align(Alignment.CenterStart)
                    .offset(x = Dimens20, y = DimensNegative20)
                    .rotate(rotation)
            ) {
                // Outer reel
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    ReelLight,
                                    ReelDark
                                )
                            ),
                            shape = CircleShape
                        )
                ) {
                    // Center hole
                    Box(
                        modifier = Modifier
                            .size(Dimens40)
                            .align(Alignment.Center)
                            .background(ReelCenter, CircleShape)
                    )

                    // Reel holes
                    val REEL_HOLE_COUNT = 6
                    val angles = listOf(0f, 60f, 120f, 180f, 240f, 300f)
                    angles.forEach { angle ->
                        Box(
                            modifier = Modifier
                                .size(Dimens16)
                                .align(Alignment.Center)
                                .offset(
                                    x = (35 * cos(Math.toRadians(angle.toDouble()))).dp,
                                    y = (35 * sin(Math.toRadians(angle.toDouble()))).dp
                                )
                                .background(ReelCenter, CircleShape)
                        )
                    }
                }
            }

            // Popcorn container
            Box(
                modifier = Modifier
                    .width(Dimens80)
                    .height(Dimens100)
                    .align(Alignment.CenterEnd)
                    .offset(x = DimensNegative20, y = DimensNegative10)
            ) {
                // Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens80)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    LoadingRed,
                                    LoadingRedDark
                                )
                            )
                        )
                ) {
                    // Red and white stripes
                    val STRIPE_COUNT = 4
                    repeat(STRIPE_COUNT) { index ->
                        if (index % 2 == 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(Dimens20)
                                    .offset(y = (index * 20).dp)
                                    .background(Color.White)
                            )
                        }
                    }
                }

                                    // Popcorn pieces
                    val POPCORN_COUNT = 6
                    val popcornPositions = listOf(
                        Pair(Dimens10, Dimens0),
                        Pair(Dimens30, DimensNegative5),
                        Pair(Dimens50, Dimens2),
                        Pair(Dimens20, DimensNegative8),
                        Pair(Dimens45, DimensNegative10),
                        Pair(Dimens35, DimensNegative15)
                    )

                popcornPositions.forEach { (x, y) ->
                    Box(
                        modifier = Modifier
                            .size(Dimens12)
                            .offset(x = x, y = y)
                            .background(
                                LoadingOrange,
                                RoundedCornerShape(Dimens6)
                            )
                    )
                }
            }
        }

        // Loading section at bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .padding(bottom = Dimens40)
        ) {
            Text(
                text = "Loading",
                fontSize = Typography18,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(bottom = Dimens12)
            )

            // Progress bar background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(Dimens4)
                    .background(
                        ReelCenter,
                        RoundedCornerShape(Dimens2)
                    )
            ) {
                // Progress indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(
                            LoadingBlue,
                            RoundedCornerShape(Dimens2)
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieLoadingScreenPreview() {
    MaterialTheme {
        MovieLoadingScreen(progress = Alpha03)
    }
}