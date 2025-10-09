package org.studioapp.cinemy.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailViewModel
import org.studioapp.cinemy.presentation.movieslist.MoviesListViewModel

/**
 * Koin module for presentation layer dependency injection
 * Provides ViewModels and other presentation-related dependencies
 */
val presentationModule = module {

    // ViewModels
    viewModel { MoviesListViewModel(get()) }
    viewModel { MovieDetailViewModel(get(), get()) }
}
