package org.studioapp.cinemy.di

import org.studioapp.cinemy.ml.SentimentAnalyzer
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val mlModule = module {
    single { SentimentAnalyzer.getInstance(androidContext()) }
}
