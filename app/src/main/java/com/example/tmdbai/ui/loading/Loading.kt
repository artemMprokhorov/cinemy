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
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.tmdbai.ui.theme.Dimens0
import com.example.tmdbai.ui.theme.Dimens1
import com.example.tmdbai.ui.theme.Dimens10
import com.example.tmdbai.ui.theme.Dimens100
import com.example.tmdbai.ui.theme.Dimens12
import com.example.tmdbai.ui.theme.Dimens120
import com.example.tmdbai.ui.theme.Dimens16
import com.example.tmdbai.ui.theme.Dimens2
import com.example.tmdbai.ui.theme.Dimens3
import com.example.tmdbai.ui.theme.Dimens20
import com.example.tmdbai.ui.theme.Dimens30
import com.example.tmdbai.ui.theme.Dimens280
import com.example.tmdbai.ui.theme.Dimens300
import com.example.tmdbai.ui.theme.Dimens32
import com.example.tmdbai.ui.theme.Dimens35
import com.example.tmdbai.ui.theme.Dimens4
import com.example.tmdbai.ui.theme.Dimens40
import com.example.tmdbai.ui.theme.Dimens45
import com.example.tmdbai.ui.theme.Dimens50
import com.example.tmdbai.ui.theme.Dimens6
import com.example.tmdbai.ui.theme.Dimens8
import com.example.tmdbai.ui.theme.Dimens80
import com.example.tmdbai.ui.theme.DimensNegative10
import com.example.tmdbai.ui.theme.DimensNegative15
import com.example.tmdbai.ui.theme.DimensNegative20
import com.example.tmdbai.ui.theme.DimensNegative40
import com.example.tmdbai.ui.theme.DimensNegative5
import com.example.tmdbai.ui.theme.DimensNegative80
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
import kotlin.math.cos
import kotlin.math.sin
import kotlinx.coroutines.delay

@Composable
fun Clapperboard(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        // Top section with pattern (clapper sticks area)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(Dimens20)
                .background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF2A2A2A), // Dark grey
                            Color(0xFF1A1A1A), // Black
                            Color(0xFF2A2A2A), // Dark grey
                            Color(0xFF1A1A1A)  // Black
                        )
                    )
                )
        ) {
            // Three dots on the left (hinge/bolt)
            Row(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = Dimens8),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(Dimens2)
            ) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .size(Dimens3)
                            .background(Color.Black, CircleShape)
                    )
                }
            }
        }
        
        // Main slate area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    Color(0xFF3A3A3A), // Dark grey background
                    RoundedCornerShape(
                        bottomStart = Dimens8,
                        bottomEnd = Dimens8
                    )
                )
                .padding(Dimens12)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(Dimens8)
            ) {
                // PROD.NO. row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "PROD.NO.",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
                
                // SCENE, TAKE, ROLL row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "SCENE",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "TAKE",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ROLL",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
                
                // DATE, SOUND row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "DATE",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "SOUND",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
                
                // PROD.CO. row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "PROD.CO.",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
                
                // DIRECTOR row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "DIRECTOR",
                        color = Color.White,
                        fontSize = Typography18,
                        fontWeight = FontWeight.Bold
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens1)
                        .background(Color(0xFF666666))
                )
            }
        }
    }
}

@Composable
fun MovieLoadingScreen(
    progress: Float = Alpha03,
    onLoadingComplete: () -> Unit = {}
) {
    var currentProgress by remember { mutableStateOf(0f) }
    
    LaunchedEffect(key1 = true) {
        val totalDuration = 5000L // 5 seconds
        val updateInterval = 50L // Update every 50ms for smooth animation
        val progressIncrement = updateInterval.toFloat() / totalDuration.toFloat()
        
        while (currentProgress < 1f) {
            delay(updateInterval)
            currentProgress += progressIncrement
            if (currentProgress > 1f) currentProgress = 1f
        }
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
        ) {
            // Film Clapperboard
            Clapperboard(
                modifier = Modifier
                    .size(Dimens280)
                    .align(Alignment.Center)
            )
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
                        .fillMaxWidth(currentProgress)
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