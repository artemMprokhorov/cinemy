package org.studioapp.cinemy.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Test
import org.studioapp.cinemy.data.remote.dto.ColorMetadataDto
import org.studioapp.cinemy.data.remote.dto.MovieColorsDto
import org.studioapp.cinemy.data.remote.dto.MovieDto
import org.studioapp.cinemy.data.remote.dto.MovieListResponseDto

class MovieMapperTest {

    @Test
    fun `mapMovieDtoToMovie maps all new fields correctly`() {
        // Given
        val movieDto = MovieDto(
            id = 1311031,
            title = "Demon Slayer: Kimetsu no Yaiba Infinity Castle",
            description = "The Demon Slayer Corps are drawn into the Infinity Castle...",
            posterPath = "/sUsVimPdA1l162FvdBIlmKBlWHx.jpg",
            rating = 7.769,
            voteCount = 333,
            releaseDate = "2025-07-18",
            backdropPath = "/j7MKdRIwfJejNlJQG1hzjFJmtlH.jpg",
            genreIds = listOf(16, 28, 14, 53),
            popularity = 668.2088,
            adult = false,
            originalLanguage = "ja",
            originalTitle = "劇場版「鬼滅の刃」無限城編 第一章 猗窩座再来",
            video = false,
            colors = MovieColorsDto(
                accent = "#3AA1EF",
                primary = "#1278D4",
                secondary = "#238EE5",
                metadata = ColorMetadataDto(
                    category = "MEDIUM",
                    modelUsed = true,
                    rating = 7.769
                )
            )
        )

        // When
        val movie = MovieMapper.mapMovieDtoToMovie(movieDto)

        // Then
        assertEquals(1311031, movie.id)
        assertEquals("Demon Slayer: Kimetsu no Yaiba Infinity Castle", movie.title)
        assertEquals("ja", movie.originalLanguage)
        assertEquals("劇場版「鬼滅の刃」無限城編 第一章 猗窩座再来", movie.originalTitle)
        assertEquals(false, movie.video)
        assertEquals("#3AA1EF", movie.colors.accent)
        assertEquals("#1278D4", movie.colors.primary)
        assertEquals("#238EE5", movie.colors.secondary)
        assertEquals("MEDIUM", movie.colors.metadata.category)
        assertEquals(true, movie.colors.metadata.modelUsed)
        assertEquals(7.769, movie.colors.metadata.rating, 0.001)
    }

    @Test
    fun `mapMovieListResponseDtoToMovieListResponse maps new contract structure correctly`() {
        // Given
        val movieDto = MovieDto(
            id = 1311031,
            title = "Test Movie",
            description = "Test Description",
            posterPath = "/test.jpg",
            rating = 7.5,
            voteCount = 100,
            releaseDate = "2025-01-01",
            backdropPath = "/backdrop.jpg",
            genreIds = listOf(16, 28),
            popularity = 100.0,
            adult = false,
            originalLanguage = "en",
            originalTitle = "Test Movie",
            video = false,
            colors = MovieColorsDto(
                accent = "#3AA1EF",
                primary = "#1278D4",
                secondary = "#238EE5",
                metadata = ColorMetadataDto(
                    category = "MEDIUM",
                    modelUsed = true,
                    rating = 7.5
                )
            )
        )

        val responseDto = MovieListResponseDto(
            page = 1,
            results = listOf(movieDto),
            totalPages = 52604,
            totalResults = 1052063
        )

        // When
        val response = MovieMapper.mapMovieListResponseDtoToMovieListResponse(responseDto)

        // Then
        assertEquals(1, response.page)
        assertEquals(1, response.results.size)
        assertEquals(52604, response.totalPages)
        assertEquals(1052063, response.totalResults)
        assertEquals("Test Movie", response.results[0].title)
        assertEquals("en", response.results[0].originalLanguage)
        assertEquals("MEDIUM", response.results[0].colors.metadata.category)
    }
}