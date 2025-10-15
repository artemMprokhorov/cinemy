package org.studioapp.cinemy.ml.mlfactory

import org.studioapp.cinemy.ml.MLConstants.SENTIMENT_POSITIVE
import org.studioapp.cinemy.ml.MLConstants.SENTIMENT_NEGATIVE
import org.studioapp.cinemy.ml.MLConstants.SENTIMENT_NEUTRAL

/**
 * Factory for creating multilingual sentiment analysis keywords.
 * 
 * This factory provides centralized keyword management for sentiment analysis across
 * multiple languages (English, Spanish, Russian). It serves as the primary source
 * for sentiment keywords used in keyword-based sentiment analysis models throughout
 * the ML layer.
 * 
 * The factory supports three sentiment categories:
 * - Positive: Words expressing positive sentiment (amazing, fantastic, great, etc.)
 * - Negative: Words expressing negative sentiment (terrible, awful, horrible, etc.)
 * - Neutral: Words expressing neutral sentiment (okay, decent, average, etc.)
 * 
 * Each category includes keywords in three languages to support multilingual
 * sentiment analysis with consistent accuracy across different language inputs.
 */
object KeywordFactory {

    /**
     * Creates multilingual keywords for sentiment analysis.
     * 
     * This method provides a centralized way to retrieve sentiment keywords
     * for any of the three supported sentiment types. It returns a comprehensive
     * list of keywords in multiple languages (English, Spanish, Russian) that
     * are used for keyword-based sentiment analysis.
     * 
     * The method supports three sentiment types:
     * - "positive": Returns positive sentiment keywords (amazing, fantastic, etc.)
     * - "negative": Returns negative sentiment keywords (terrible, awful, etc.)
     * - "neutral": Returns neutral sentiment keywords (okay, decent, etc.)
     * 
     * For unsupported sentiment types, an empty list is returned.
     * 
     * @param type Sentiment type string ("positive", "negative", or "neutral")
     * @return List of sentiment keywords in multiple languages, or empty list for unsupported types
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
     * Creates positive sentiment keywords in multiple languages.
     * 
     * This method provides a comprehensive list of positive sentiment keywords
     * in three languages (English, Spanish, Russian) for sentiment analysis.
     * The keywords include various intensity levels from basic positive words
     * to strong positive expressions.
     * 
     * Language coverage:
     * - English: 30 keywords (amazing, fantastic, great, excellent, etc.)
     * - Spanish: 17 keywords (increíble, fantástico, excelente, etc.)
     * - Russian: 17 keywords (потрясающий, фантастический, отличный, etc.)
     * 
     * @return List of positive sentiment keywords in multiple languages
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
     * Creates negative sentiment keywords in multiple languages.
     * 
     * This method provides a comprehensive list of negative sentiment keywords
     * in three languages (English, Spanish, Russian) for sentiment analysis.
     * The keywords include various intensity levels from mild negative words
     * to strong negative expressions.
     * 
     * Language coverage:
     * - English: 30 keywords (terrible, awful, horrible, bad, etc.)
     * - Spanish: 11 keywords (terrible, horrible, malo, peor, etc.)
     * - Russian: 6 keywords (ужасный, отвратительный, плохой, etc.)
     * 
     * @return List of negative sentiment keywords in multiple languages
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
     * Creates neutral sentiment keywords in multiple languages.
     * 
     * This method provides a comprehensive list of neutral sentiment keywords
     * in three languages (English, Spanish, Russian) for sentiment analysis.
     * The keywords represent neutral or moderate sentiment expressions that
     * don't strongly indicate positive or negative sentiment.
     * 
     * Language coverage:
     * - English: 12 keywords (okay, decent, average, fine, etc.)
     * - Spanish: 5 keywords (bien, decente, promedio, aceptable, etc.)
     * - Russian: 4 keywords (нормально, приличный, средний, etc.)
     * 
     * @return List of neutral sentiment keywords in multiple languages
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
