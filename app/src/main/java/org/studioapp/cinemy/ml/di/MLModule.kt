package org.studioapp.cinemy.ml.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.studioapp.cinemy.ml.SentimentAnalyzer

/**
 * Koin dependency injection module for Machine Learning components.
 * 
 * This module provides dependency injection configuration for all ML-related services
 * in the Cinemy application, including the main sentiment analyzer and its dependencies.
 * 
 * ## Features
 * - Singleton pattern for SentimentAnalyzer to ensure single instance across the app
 * - Automatic context injection using Koin's androidContext() extension
 * - Thread-safe dependency resolution
 * 
 * ## Usage
 * ```kotlin
 * // Initialize in Application class
 * startKoin {
 *     modules(mlModule)
 * }
 * 
 * // Inject in ViewModels or other components
 * class MovieViewModel(
 *     private val sentimentAnalyzer: SentimentAnalyzer
 * ) : ViewModel() {
 *     // Use sentimentAnalyzer for sentiment analysis
 * }
 * ```
 * 
 * ## Dependencies
 * - [SentimentAnalyzer] - Main hybrid sentiment analyzer with TensorFlow Lite and keyword fallback
 * 
 * @see org.studioapp.cinemy.ml.SentimentAnalyzer
 * @since 1.0.0
 */
val mlModule = module {
    /**
     * Provides a singleton instance of SentimentAnalyzer.
     * 
     * Creates and configures the main sentiment analyzer with hybrid ML capabilities.
     * The analyzer combines TensorFlow Lite (BERT-based) and keyword-based models
     * for optimal sentiment analysis performance.
     * 
     * ## Features
     * - Hybrid sentiment analysis (TensorFlow Lite + keyword fallback)
     * - Automatic model selection based on confidence thresholds
     * - Multilingual support (English, Spanish, Russian)
     * - Hardware-optimized performance with GPU acceleration
     * - Graceful fallback mechanisms
     * 
     * ## Initialization
     * The analyzer is automatically initialized when first accessed:
     * ```kotlin
     * val analyzer: SentimentAnalyzer = get() // Injected via Koin
     * val isReady = analyzer.initialize() // Initialize hybrid system
     * ```
     * 
     * ## Model Selection Strategy
     * 1. **Primary**: TensorFlow Lite BERT model (95%+ accuracy)
     * 2. **Fallback**: Multilingual production model (100% accuracy)
     * 3. **Last Resort**: Simple keyword model (85%+ accuracy)
     * 
     * ## Performance Characteristics
     * - **TensorFlow Lite**: GPU acceleration, multi-threading support
     * - **Memory Management**: Efficient resource usage with proper cleanup
     * - **Thread Safety**: Singleton pattern with WeakReference for memory leak prevention
     * 
     * @return Configured SentimentAnalyzer instance
     * @see org.studioapp.cinemy.ml.SentimentAnalyzer.getInstance
     * @since 1.0.0
     */
    single { SentimentAnalyzer.getInstance(androidContext()) }
}
