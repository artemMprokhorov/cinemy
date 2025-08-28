@Composable
fun MovieLoadingScreen(
    progress: Float = 0.3f
) {
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
                        Color(0xFF1C1C1C),
                        Color(0xFF2B3A4B)
                    )
                )
            )
            .padding(32.dp)
    ) {
        // Movie scene illustration
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.Center)
                .offset(y = (-40).dp)
        ) {
            // Film strip background
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(40.dp)
                    .align(Alignment.BottomCenter)
                    .background(
                        Color(0xFF2C2C2C),
                        RoundedCornerShape(4.dp)
                    )
            ) {
                // Film strip holes
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(8) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    Color(0xFF1C1C1C),
                                    CircleShape
                                )
                        )
                    }
                }
            }

            // Film reel
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterStart)
                    .offset(x = 20.dp, y = (-20).dp)
                    .rotate(rotation)
            ) {
                // Outer reel
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color(0xFFE0E0E0),
                                    Color(0xFFBDBDBD)
                                )
                            ),
                            shape = CircleShape
                        )
                ) {
                    // Center hole
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .align(Alignment.Center)
                            .background(Color(0xFF424242), CircleShape)
                    )
                    
                    // Reel holes
                    val angles = listOf(0f, 60f, 120f, 180f, 240f, 300f)
                    angles.forEach { angle ->
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.Center)
                                .offset(
                                    x = (35 * cos(Math.toRadians(angle.toDouble()))).dp,
                                    y = (35 * sin(Math.toRadians(angle.toDouble()))).dp
                                )
                                .background(Color(0xFF424242), CircleShape)
                        )
                    }
                }
            }

            // Popcorn container
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(100.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-20).dp, y = (-10).dp)
            ) {
                // Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .align(Alignment.BottomCenter)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFE53935),
                                    Color(0xFFD32F2F)
                                )
                            )
                        )
                ) {
                    // Red and white stripes
                    repeat(4) { index ->
                        if (index % 2 == 1) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(20.dp)
                                    .offset(y = (index * 20).dp)
                                    .background(Color.White)
                            )
                        }
                    }
                }
                
                // Popcorn pieces
                val popcornPositions = listOf(
                    Pair(10.dp, 0.dp), Pair(30.dp, (-5).dp), Pair(50.dp, 2.dp),
                    Pair(20.dp, (-8).dp), Pair(45.dp, (-10).dp), Pair(35.dp, (-15).dp)
                )
                
                popcornPositions.forEach { (x, y) ->
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .offset(x = x, y = y)
                            .background(
                                Color(0xFFFFF3E0),
                                RoundedCornerShape(6.dp)
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
                .padding(bottom = 40.dp)
        ) {
            Text(
                text = "Loading",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Progress bar background
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .background(
                        Color(0xFF424242),
                        RoundedCornerShape(2.dp)
                    )
            ) {
                // Progress indicator
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(
                            Color(0xFF2196F3),
                            RoundedCornerShape(2.dp)
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
        MovieLoadingScreen(progress = 0.3f)
    }
}