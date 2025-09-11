package org.studioapp.cinemy.ml

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.studioapp.cinemy.BuildConfig
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * ML components performance monitoring
 */
class MLPerformanceMonitor private constructor() {

    companion object {
        private const val TAG = "MLPerformanceMonitor"

        // Performance thresholds
        private const val SHORT_TEXT_THRESHOLD = 50
        private const val MEDIUM_TEXT_THRESHOLD = 200
        private const val LOG_INTERVAL = 10

        // Text length categories
        private const val CATEGORY_SHORT = "short"
        private const val CATEGORY_MEDIUM = "medium"
        private const val CATEGORY_LONG = "long"

        // Log messages
        private const val LOG_PERFORMANCE_STATS = "ML Performance Stats:"
        private const val LOG_TOTAL_ANALYSES = "  Total analyses: "
        private const val LOG_AVERAGE_TIME = "  Average time: "
        private const val LOG_ERROR_RATE = "  Error rate: "
        private const val LOG_GROUP_STATS = "  "
        private const val LOG_ERROR_LOGGING = "Error logging performance stats"

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
     * Record analysis execution metric
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

        // Group by text length
        val lengthGroup = when {
            textLength <= SHORT_TEXT_THRESHOLD -> CATEGORY_SHORT
            textLength <= MEDIUM_TEXT_THRESHOLD -> CATEGORY_MEDIUM
            else -> CATEGORY_LONG
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

        // Log every 10 analyses
        if (analysisCount.get() % LOG_INTERVAL == 0) {
            logPerformanceStats()
        }
    }

    /**
     * Get performance statistics
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
     * Reset statistics
     */
    fun resetStats() {
        analysisCount.set(0)
        totalProcessingTime.set(0)
        errorCount.set(0)
        performanceData.clear()
    }

    /**
     * Log statistics
     */
    private fun logPerformanceStats() {
        if (!BuildConfig.DEBUG) return

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val stats = getPerformanceStats()

                stats.detailedMetrics.forEach { (group, metric) ->
                    val avgTime = metric.totalTime.toDouble() / metric.count
                }
            }.onFailure { e ->
            }
        }
    }
}

/**
 * Performance metric for group
 */
data class PerformanceMetric(
    var count: Int,
    var totalTime: Long,
    var minTime: Long,
    var maxTime: Long
)

/**
 * Overall performance statistics
 */
data class PerformanceStats(
    val totalAnalyses: Int,
    val averageProcessingTimeMs: Double,
    val errorRate: Double,
    val detailedMetrics: Map<String, PerformanceMetric>
)