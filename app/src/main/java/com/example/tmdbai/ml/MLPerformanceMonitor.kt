package com.example.tmdbai.ml

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Мониторинг производительности ML компонентов
 */
class MLPerformanceMonitor private constructor() {
    
    companion object {
        private const val TAG = "MLPerformanceMonitor"
        
        @Volatile
        private var INSTANCE: MLPerformanceMonitor? = null
        
        fun getInstance(): MLPerformanceMonitor {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: MLPerformanceMonitor().also { INSTANCE = it }
            }
        }
    }
    
    private val analysisCount = AtomicInteger(0)
    private val totalProcessingTime = AtomicLong(0)
    private val errorCount = AtomicInteger(0)
    private val performanceData = ConcurrentHashMap<String, PerformanceMetric>()
    
    /**
     * Записать метрику выполнения анализа
     */
    fun recordAnalysis(
        textLength: Int,
        processingTimeMs: Long,
        isSuccess: Boolean,
        resultType: SentimentType
    ) {
        analysisCount.incrementAndGet()
        totalProcessingTime.addAndGet(processingTimeMs)
        
        if (!isSuccess) {
            errorCount.incrementAndGet()
        }
        
        // Группируем по длине текста
        val lengthGroup = when {
            textLength <= 50 -> "short"
            textLength <= 200 -> "medium"
            else -> "long"
        }
        
        performanceData.compute(lengthGroup) { _, existing ->
            existing?.apply {
                count++
                totalTime += processingTimeMs
                if (processingTimeMs > maxTime) maxTime = processingTimeMs
                if (processingTimeMs < minTime) minTime = processingTimeMs
            } ?: PerformanceMetric(
                count = 1,
                totalTime = processingTimeMs,
                minTime = processingTimeMs,
                maxTime = processingTimeMs
            )
        }
        
        // Логируем каждые 10 анализов
        if (analysisCount.get() % 10 == 0) {
            logPerformanceStats()
        }
    }
    
    /**
     * Получить статистику производительности
     */
    fun getPerformanceStats(): PerformanceStats {
        val count = analysisCount.get()
        val avgTime = if (count > 0) totalProcessingTime.get().toDouble() / count else 0.0
        
        return PerformanceStats(
            totalAnalyses = count,
            averageProcessingTimeMs = avgTime,
            errorRate = if (count > 0) errorCount.get().toDouble() / count else 0.0,
            detailedMetrics = performanceData.toMap()
        )
    }
    
    /**
     * Сбросить статистику
     */
    fun resetStats() {
        analysisCount.set(0)
        totalProcessingTime.set(0)
        errorCount.set(0)
        performanceData.clear()
    }
    
    /**
     * Логирование статистики
     */
    private fun logPerformanceStats() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val stats = getPerformanceStats()
                Log.i(TAG, "ML Performance Stats:")
                Log.i(TAG, "  Total analyses: ${stats.totalAnalyses}")
                Log.i(TAG, "  Average time: ${stats.averageProcessingTimeMs.toInt()}ms")
                Log.i(TAG, "  Error rate: ${(stats.errorRate * 100).toInt()}%")
                
                stats.detailedMetrics.forEach { (group, metric) ->
                    val avgTime = metric.totalTime.toDouble() / metric.count
                    Log.i(TAG, "  $group: ${metric.count} analyses, avg ${avgTime.toInt()}ms")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error logging performance stats", e)
            }
        }
    }
}

/**
 * Метрика производительности для группы
 */
data class PerformanceMetric(
    var count: Int,
    var totalTime: Long,
    var minTime: Long,
    var maxTime: Long
)

/**
 * Общая статистика производительности
 */
data class PerformanceStats(
    val totalAnalyses: Int,
    val averageProcessingTimeMs: Double,
    val errorRate: Double,
    val detailedMetrics: Map<String, PerformanceMetric>
)