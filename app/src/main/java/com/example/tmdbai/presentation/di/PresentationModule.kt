package com.example.tmdbai.presentation.di

import com.example.tmdbai.presentation.moviedetail.MovieDetailViewModel
import com.example.tmdbai.presentation.movieslist.MoviesListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    // ViewModels
    viewModel { MoviesListViewModel(get()) }
    viewModel { MovieDetailViewModel(get()) }
}
