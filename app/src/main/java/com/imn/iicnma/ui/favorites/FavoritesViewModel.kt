package com.imn.iicnma.ui.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.imn.iicnma.data.repository.movies.MovieRepository

class FavoritesViewModel @ViewModelInject constructor(
    val movieRepository: MovieRepository
) : ViewModel() {
    val movies = movieRepository.getFavoriteMovies()
}