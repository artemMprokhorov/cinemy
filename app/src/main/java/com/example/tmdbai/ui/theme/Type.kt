package com.example.tmdbai.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = Typography16,
        lineHeight = Typography24,
        letterSpacing = LetterSpacing05
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
            fontSize = Typography22,
    lineHeight = Typography28,
        letterSpacing = LetterSpacing0
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
            fontSize = Typography11,
    lineHeight = Typography16,
        letterSpacing = LetterSpacing05
    )
    */
)