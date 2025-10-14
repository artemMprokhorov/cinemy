package org.studioapp.cinemy.ml.di

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.studioapp.cinemy.ml.SentimentAnalyzer

val mlModule = module {
    single { SentimentAnalyzer.getInstance(androidContext()) }
}
