data class Movie(
    val title: String,
    val description: String,
    val posterColor: Color,
    val posterImageUrl: String? = null
)

@Composable
fun MoviesListScreen() {
    val movies = remember {
        listOf(
            Movie(
                title = "The Midnight Bloom",
                description = "A young botanist discovers a rare, bioluminescent flower with the power to heal any ailment, but must protect it from those who seek to exploit its magic.",
                posterColor = Color(0xFF4A90A4)
            ),
            Movie(
                title = "Echoes of the Past",
                description = "A historian uncovers a hidden diary revealing a forgotten chapter of the city's history, leading to a quest for a lost artifact.",
                posterColor = Color(0xFF8B7355)
            ),
            Movie(
                title = "The Last Starfarer",
                description = "In a distant future, a lone pilot embarks on a perilous journey to the edge of the galaxy to deliver a message that could save humanity.",
                posterColor = Color(0xFF2C5F6F)
            ),
            Movie(
                title = "Whispers of the Wind",
                description = "A reclusive artist living in a remote mountain village finds inspiration in the wind's whispers, creating breathtaking sculptures that capture the essence of nature.",
                posterColor = Color(0xFF5A7C65)
            ),
            Movie(
                title = "The Silent Guardian",
                description = "A mysterious figure watches over a bustling metropolis, silently intervening to protect its citizens from unseen threats.",
                posterColor = Color(0xFF4F6B8E)
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B2631))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Movies",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Movies list
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(32.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(movies) { movie ->
                    MovieItem(movie = movie)
                }
            }
        }
    }
}

@Composable
fun MovieItem(movie: Movie) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // Left content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // Trending tag
            Text(
                text = "Trending",
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Movie title
            Text(
                text = movie.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Movie description
            Text(
                text = movie.description,
                fontSize = 14.sp,
                color = Color(0xFFB0BEC5),
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // More Details button
            Button(
                onClick = { /* Handle more details */ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF37474F)
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "More Details",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }
        
        // Movie poster
        MoviePoster(
            color = movie.posterColor,
            modifier = Modifier
                .width(100.dp)
                .height(140.dp)
        )
    }
}

@Composable
fun MoviePoster(
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        color.copy(alpha = 0.8f),
                        color
                    )
                )
            )
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.2f),
                shape = RoundedCornerShape(12.dp)
            )
    ) {
        // Placeholder for movie poster image
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            // Simple movie icon placeholder
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color.White.copy(alpha = 0.3f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        // Gradient overlay at bottom for text readability
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.6f)
                        )
                    )
                )
        )
        
        // Movie title on poster (small text)
        Text(
            text = "POSTER",
            fontSize = 8.sp,
            color = Color.White.copy(alpha = 0.7f),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MoviesListScreenPreview() {
    MaterialTheme {
        MoviesListScreen()
    }
}