package org.studioapp.cinemy.ml.model

/**
 * Information about a sentiment analysis model
 * @param type Model type identifier
 * @param version Model version
 * @param language Supported languages
 * @param accuracy Model accuracy percentage
 * @param speed Model processing speed
 */
data class ModelInfo(
    val type: String,
    val version: String,
    val language: String,
    val accuracy: String,
    val speed: String
)
