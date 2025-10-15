package org.studioapp.cinemy.ml.mlfactory

import org.studioapp.cinemy.ml.MLConstants.SENTIMENT_POSITIVE
import org.studioapp.cinemy.ml.MLConstants.SENTIMENT_NEGATIVE
import org.studioapp.cinemy.ml.MLConstants.SENTIMENT_NEUTRAL

/**
 * Factory for creating multilingual sentiment analysis keywords
 * Provides centralized keyword management for sentiment analysis
 */
object KeywordFactory {

    /**
     * Creates multilingual keywords for sentiment analysis
     * 
     * @param type Sentiment type (positive, negative, neutral)
     * @return List of keywords in multiple languages
     */
    fun createMultilingualKeywords(type: String): List<String> {
        return when (type) {
            SENTIMENT_POSITIVE -> createPositiveKeywords()
            SENTIMENT_NEGATIVE -> createNegativeKeywords()
            SENTIMENT_NEUTRAL -> createNeutralKeywords()
            else -> emptyList()
        }
    }

    /**
     * Creates positive sentiment keywords in multiple languages
     */
    private fun createPositiveKeywords(): List<String> {
        return listOf(
            // English
            "amazing", "fantastic", "great", "excellent", "wonderful", "brilliant",
            "outstanding", "superb", "magnificent", "perfect", "incredible", "awesome",
            "beautiful", "lovely", "good", "nice", "best", "favorite", "love", "enjoy",
            "phenomenal", "spectacular", "remarkable", "exceptional", "marvelous",
            "stunning", "impressive", "captivating", "engaging", "compelling",
            // Spanish
            "increíble", "fantástico", "excelente", "maravilloso", "brillante",
            "sobresaliente", "magnífico", "perfecto", "asombroso", "impresionante",
            "hermoso", "encantador", "bueno", "mejor", "favorito", "amar", "disfrutar",
            // Russian
            "потрясающий", "фантастический", "отличный", "замечательный", "блестящий",
            "выдающийся", "великолепный", "идеальный", "невероятный", "впечатляющий",
            "красивый", "прекрасный", "хороший", "лучший", "любимый", "любить", "наслаждаться"
        )
    }

    /**
     * Creates negative sentiment keywords in multiple languages
     */
    private fun createNegativeKeywords(): List<String> {
        return listOf(
            // English
            "terrible", "awful", "horrible", "bad", "worst", "hate", "disgusting",
            "boring", "stupid", "dumb", "annoying", "frustrating", "disappointing",
            "waste", "rubbish", "garbage", "trash", "sucks", "pathetic", "lame",
            "atrocious", "dreadful", "appalling", "mediocre", "unwatchable",
            "cringe", "cheesy", "predictable", "cliché", "overrated",
            // Spanish
            "terrible", "horrible", "malo", "peor", "odiar", "asqueroso",
            "aburrido", "estúpido", "molesto", "frustrante", "decepcionante",
            "basura", "patético", "atroz", "espantoso", "mediocre",
            // Russian
            "ужасный", "отвратительный", "плохой", "худший", "ненавидеть", "мерзкий",
            "скучный", "глупый", "раздражающий", "разочаровывающий",
            "мусор", "жалкий", "отвратительный", "ужасный", "посредственный"
        )
    }

    /**
     * Creates neutral sentiment keywords in multiple languages
     */
    private fun createNeutralKeywords(): List<String> {
        return listOf(
            // English
            "okay", "decent", "average", "fine", "acceptable", "reasonable",
            "standard", "typical", "normal", "ordinary", "mediocre", "so-so",
            // Spanish
            "bien", "decente", "promedio", "aceptable", "razonable",
            "estándar", "típico", "normal", "ordinario", "mediocre",
            // Russian
            "нормально", "приличный", "средний", "приемлемый", "разумный",
            "стандартный", "типичный", "обычный", "посредственный"
        )
    }
}
