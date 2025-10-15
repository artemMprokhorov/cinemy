package org.studioapp.cinemy.presentation.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import org.studioapp.cinemy.presentation.moviedetail.MovieDetailViewModel
import org.studioapp.cinemy.presentation.movieslist.MoviesListViewModel

/**
 * Koin module for presentation layer dependency injection.
 * 
 * This module provides ViewModels and other presentation-related dependencies
 * for the Cinemy application. It defines the dependency injection configuration
 * for the presentation layer components following the MVI architecture pattern.
 * 
 * The module includes:
 * - MoviesListViewModel: Handles movies list screen state and business logic
 * - MovieDetailViewModel: Manages movie details screen with ML integration
 * 
 * Dependencies are automatically resolved through Koin's dependency injection
 * framework, ensuring proper separation of concerns and testability.
 */
val presentationModule = module {

    // ViewModels
    viewModel { MoviesListViewModel(get()) }
    viewModel { MovieDetailViewModel(get(), get()) }
}
