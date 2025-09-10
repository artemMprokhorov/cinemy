package com.example.tmdbai.ml

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.IOException

/**
 * Анализатор тональности на основе ключевых слов для TmdbAi
 * Быстрая и эффективная реализация для мобильных устройств
 */
class SentimentAnalyzer private constructor(private val context: Context) {
    
    private var model: KeywordSentimentModel? = null
    private var isInitialized = false
    
    companion object {
        @Volatile
        private var INSTANCE: SentimentAnalyzer? = null
        
        fun getInstance(context: Context): SentimentAnalyzer {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SentimentAnalyzer(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
    
    /**
     * Инициализация анализатора (вызывать при старте приложения)
     */
    suspend fun initialize(): Boolean = withContext(Dispatchers.IO) {
        try {
            if (isInitialized) return@withContext true
            
            val modelJson = context.assets.open("ml_models/keyword_sentiment_model.json").use { inputStream ->
                inputStream.bufferedReader().readText()
            }
            
            model = createSimpleModel()
            isInitialized = true
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Анализ тональности текста
     */
    suspend fun analyzeSentiment(text: String): SentimentResult = withContext(Dispatchers.Default) {
        if (!isInitialized || model == null) {
            return@withContext SentimentResult.error("Анализатор не инициализирован")
        }
        
        if (text.isBlank()) {
            return@withContext SentimentResult.neutral()
        }
        
        try {
            analyzeWithKeywords(text, model!!)
        } catch (e: Exception) {
            SentimentResult.error("Ошибка анализа: ${e.message}")
        }
    }
    
    /**
     * Пакетный анализ нескольких текстов
     */
    suspend fun analyzeBatch(texts: List<String>): List<SentimentResult> = withContext(Dispatchers.Default) {
        texts.map { text -> analyzeSentiment(text) }
    }
    
    /**
     * Получение информации о модели
     */
    fun getModelInfo(): ModelInfo? = model?.modelInfo
    
    /**
     * Создание простой модели для тестирования
     */
    private fun createSimpleModel(): KeywordSentimentModel {
        val modelInfo = ModelInfo(
            type = "keyword_sentiment_analysis",
            version = "1.0.0",
            language = "en",
            accuracy = "~80%",
            speed = "very_fast"
        )
        
        val positiveKeywords = listOf(
            "amazing", "fantastic", "great", "excellent", "wonderful", "brilliant",
            "outstanding", "superb", "magnificent", "perfect", "incredible", "awesome",
            "beautiful", "lovely", "good", "nice", "best", "favorite", "love", "enjoy"
        )
        
        val negativeKeywords = listOf(
            "terrible", "awful", "horrible", "bad", "worst", "hate", "disgusting",
            "boring", "stupid", "dumb", "annoying", "frustrating", "disappointing",
            "waste", "rubbish", "garbage", "trash", "sucks", "pathetic", "lame"
        )
        
        val algorithm = AlgorithmConfig(
            defaultConfidence = 0.6,
            positiveWeight = 1.0,
            negativeWeight = 1.0,
            neutralThreshold = 0.5,
            minConfidence = 0.3,
            maxConfidence = 0.9
        )
        
        return KeywordSentimentModel(
            modelInfo = modelInfo,
            positiveKeywords = positiveKeywords,
            negativeKeywords = negativeKeywords,
            algorithm = algorithm
        )
    }
    
    /**
     * Основной алгоритм анализа по ключевым словам
     */
    private fun analyzeWithKeywords(text: String, model: KeywordSentimentModel): SentimentResult {
        val textLower = text.lowercase()
        val words = textLower.split(Regex("\\W+")).filter { it.isNotBlank() }
        
        var positiveScore = 0.0
        var negativeScore = 0.0
        var foundWords = mutableListOf<String>()
        
        // Подсчет положительных слов
        for (word in words) {
            if (model.positiveKeywords.contains(word)) {
                positiveScore += 1.0
                foundWords.add("+$word")
            }
            if (model.negativeKeywords.contains(word)) {
                negativeScore += 1.0
                foundWords.add("-$word")
            }
        }
        
        // Проверка модификаторов интенсивности
        model.intensityModifiers?.forEach { (modifier, multiplier) ->
            if (textLower.contains(modifier)) {
                positiveScore *= multiplier
                negativeScore *= multiplier
            }
        }
        
        // Определение результата
        val totalScore = positiveScore + negativeScore
        val algorithm = model.algorithm
        
        return when {
            totalScore == 0.0 -> SentimentResult.neutral()
            positiveScore > negativeScore -> {
                val confidence = minOf(
                    algorithm.maxConfidence,
                    algorithm.defaultConfidence + (positiveScore - negativeScore) * 0.1
                )
                SentimentResult.positive(confidence, foundWords)
            }
            negativeScore > positiveScore -> {
                val confidence = minOf(
                    algorithm.maxConfidence,
                    algorithm.defaultConfidence + (negativeScore - positiveScore) * 0.1
                )
                SentimentResult.negative(confidence, foundWords)
            }
            else -> SentimentResult.neutral()
        }
    }
}

/**
 * Результат анализа тональности
 */
data class SentimentResult(
    val sentiment: SentimentType,
    val confidence: Double,
    val isSuccess: Boolean = true,
    val errorMessage: String? = null,
    val foundKeywords: List<String> = emptyList(),
    val processingTimeMs: Long = 0L
) {
    companion object {
        fun positive(confidence: Double, keywords: List<String> = emptyList()) = 
            SentimentResult(SentimentType.POSITIVE, confidence, foundKeywords = keywords)
            
        fun negative(confidence: Double, keywords: List<String> = emptyList()) = 
            SentimentResult(SentimentType.NEGATIVE, confidence, foundKeywords = keywords)
            
        fun neutral() = 
            SentimentResult(SentimentType.NEUTRAL, 0.5)
            
        fun error(message: String) = 
            SentimentResult(SentimentType.NEUTRAL, 0.0, false, message)
    }
    
    /**
     * Возвращает человекочитаемое описание
     */
    fun getDescription(): String = when {
        !isSuccess -> "Ошибка: $errorMessage"
        sentiment == SentimentType.POSITIVE -> "Положительный (${(confidence * 100).toInt()}%)"
        sentiment == SentimentType.NEGATIVE -> "Отрицательный (${(confidence * 100).toInt()}%)"
        else -> "Нейтральный"
    }
}

/**
 * Типы тональности
 */
enum class SentimentType {
    POSITIVE, NEGATIVE, NEUTRAL
}

/**
 * Модель данных для keyword-based анализа
 */
data class KeywordSentimentModel(
    val modelInfo: ModelInfo,
    val positiveKeywords: List<String>,
    val negativeKeywords: List<String>,
    val neutralIndicators: List<String>? = null,
    val intensityModifiers: Map<String, Double>? = null,
    val algorithm: AlgorithmConfig
)

data class ModelInfo(
    val type: String,
    val version: String,
    val language: String,
    val accuracy: String,
    val speed: String
)

data class AlgorithmConfig(
    val defaultConfidence: Double,
    val positiveWeight: Double,
    val negativeWeight: Double,
    val neutralThreshold: Double,
    val minConfidence: Double,
    val maxConfidence: Double
)