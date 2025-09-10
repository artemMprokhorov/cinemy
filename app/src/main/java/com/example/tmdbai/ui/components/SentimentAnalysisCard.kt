package com.example.tmdbai.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.tmdbai.ml.SentimentResult
import com.example.tmdbai.ml.SentimentType
import com.example.tmdbai.ui.theme.*

@Composable
fun SentimentAnalysisCard(
    sentimentResult: SentimentResult?,
    isLoading: Boolean = false,
    error: String? = null,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        shape = RoundedCornerShape(Dimens16)
    ) {
        Column(
            modifier = Modifier.padding(Dimens16),
            verticalArrangement = Arrangement.spacedBy(Dimens12)
        ) {
            Text(
                text = "🤖 Анализ тональности отзывов",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Text(
                text = "Готовые отзывы от N8N бэкенда • AI-анализ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = Float07)
            )
            
            // Отображение ошибки
            error?.let { errorText ->
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            
            // Результат анализа
            sentimentResult?.let { result ->
                SentimentResultCard(result = result)
            }
            
            // Показываем сообщение если нет результатов
            if (sentimentResult == null && !isLoading && error == null) {
                Text(
                    text = "Отзывы будут загружены с бэкенда...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = Float07)
                )
            }
        }
    }
}

@Composable
private fun SentimentResultCard(
    result: SentimentResult,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, icon) = when (result.sentiment) {
        SentimentType.POSITIVE -> Triple(
            Color(0xFF4CAF50).copy(alpha = Float01),
            Color(0xFF4CAF50),
            "😊"
        )
        SentimentType.NEGATIVE -> Triple(
            Color(0xFFF44336).copy(alpha = Float01),
            Color(0xFFF44336),
            "😞"
        )
        else -> Triple(
            Color(0xFF9E9E9E).copy(alpha = Float01),
            Color(0xFF9E9E9E),
            "😐"
        )
    }
    
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(Dimens8)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Dimens12),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = icon,
                    fontSize = 24.sp
                )
                Spacer(Modifier.width(Dimens12))
                Column {
                    Text(
                        text = result.getDescription(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor,
                        fontWeight = FontWeight.Medium
                    )
                    if (result.foundKeywords.isNotEmpty()) {
                        Text(
                            text = "Ключевые слова: ${result.foundKeywords.take(3).joinToString(", ")}",
                            style = MaterialTheme.typography.bodySmall,
                            color = textColor.copy(alpha = Float07)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SentimentAnalysisCardPreview() {
    MaterialTheme {
        SentimentAnalysisCard(
            sentimentResult = null,
            isLoading = false
        )
    }
}
