package com.example.tmdbai.di

import com.example.tmdbai.ml.SentimentAnalyzer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mlModule = module {
    single { SentimentAnalyzer.getInstance(androidContext()) }
}
