@Composable
fun MovieAppSplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B3A4B))
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Movie clapperboard icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFFFB74D),
                                Color(0xFFFF9800)
                            )
                        )
                    ),
                contentAlignment = Alignment.TopEnd
            ) {
                // Clapperboard stripes
                repeat(4) { index ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .height(4.dp)
                            .offset(y = (12 + index * 8).dp)
                            .background(
                                Color.Black,
                                RoundedCornerShape(2.dp)
                            )
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // App title
            Text(
                text = "Movie App",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontFamily = FontFamily.Default
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Subtitle
            Text(
                text = "Discover new films from TMDB",
                fontSize = 16.sp,
                color = Color(0xFF9E9E9E),
                fontFamily = FontFamily.Default
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MovieAppSplashScreenPreview() {
    MaterialTheme {
        MovieAppSplashScreen()
    }
}