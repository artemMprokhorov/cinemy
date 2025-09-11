package org.studioapp.cinemy.data.mapper

import androidx.compose.ui.graphics.Color
import org.junit.Assert.assertEquals
import org.junit.Test
import org.studioapp.cinemy.data.model.ButtonConfiguration
import org.studioapp.cinemy.data.model.ColorScheme
import org.studioapp.cinemy.data.remote.dto.ButtonConfigurationDto
import org.studioapp.cinemy.data.remote.dto.ColorSchemeDto
import org.studioapp.cinemy.data.util.TestColorUtils

/**
 * Test wrapper for MovieMapper that uses TestColorUtils instead of ColorUtils
 */
object TestMovieMapper {
    fun mapColorSchemeDtoToColorScheme(dto: ColorSchemeDto): ColorScheme {
        return ColorScheme(
            primary = TestColorUtils.parseColor(dto.primary),
            secondary = TestColorUtils.parseColor(dto.secondary),
            background = TestColorUtils.parseColor(dto.background),
            surface = TestColorUtils.parseColor(dto.surface),
            onPrimary = TestColorUtils.parseColor(dto.onPrimary),
            onSecondary = TestColorUtils.parseColor(dto.onSecondary),
            onBackground = TestColorUtils.parseColor(dto.onBackground),
            onSurface = TestColorUtils.parseColor(dto.onSurface),
            moviePosterColors = dto.moviePosterColors.map { TestColorUtils.parseColor(it) }
        )
    }

    fun mapButtonConfigurationDtoToButtonConfiguration(dto: ButtonConfigurationDto): ButtonConfiguration {
        return ButtonConfiguration(
            primaryButtonColor = TestColorUtils.parseColor(dto.primaryButtonColor),
            secondaryButtonColor = TestColorUtils.parseColor(dto.secondaryButtonColor),
            buttonTextColor = TestColorUtils.parseColor(dto.buttonTextColor),
            buttonCornerRadius = dto.buttonCornerRadius
        )
    }
}

class MovieMapperTest {

    @Test
    fun `mapColorSchemeDtoToColorScheme should map correctly`() {
        // Given
        val colorSchemeDto = ColorSchemeDto(
            primary = "#FF0000",
            secondary = "#00FF00",
            background = "#0000FF",
            surface = "#FFFF00",
            onPrimary = "#FFFFFF",
            onSecondary = "#000000",
            onBackground = "#FFFFFF",
            onSurface = "#000000",
            moviePosterColors = listOf("#FF0000", "#00FF00")
        )

        // When
        val result = TestMovieMapper.mapColorSchemeDtoToColorScheme(colorSchemeDto)

        // Then
        assertEquals(Color(0xFFFF0000), result.primary)
        assertEquals(Color(0xFF00FF00), result.secondary)
        assertEquals(Color(0xFF0000FF), result.background)
        assertEquals(Color(0xFFFFFF00), result.surface)
        assertEquals(Color(0xFFFFFFFF), result.onPrimary)
        assertEquals(Color(0xFF000000), result.onSecondary)
        assertEquals(Color(0xFFFFFFFF), result.onBackground)
        assertEquals(Color(0xFF000000), result.onSurface)
        assertEquals(2, result.moviePosterColors.size)
        assertEquals(Color(0xFFFF0000), result.moviePosterColors[0])
        assertEquals(Color(0xFF00FF00), result.moviePosterColors[1])
    }

    @Test
    fun `mapButtonConfigurationDtoToButtonConfiguration should map correctly`() {
        // Given
        val buttonConfigDto = ButtonConfigurationDto(
            primaryButtonColor = "#FF0000",
            secondaryButtonColor = "#00FF00",
            buttonTextColor = "#FFFFFF",
            buttonCornerRadius = 8
        )

        // When
        val result = TestMovieMapper.mapButtonConfigurationDtoToButtonConfiguration(buttonConfigDto)

        // Then
        assertEquals(Color(0xFFFF0000), result.primaryButtonColor)
        assertEquals(Color(0xFF00FF00), result.secondaryButtonColor)
        assertEquals(Color(0xFFFFFFFF), result.buttonTextColor)
        assertEquals(8, result.buttonCornerRadius)
    }
}