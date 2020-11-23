package com.imn.iicnma.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.imn.iicnma.data.repository.favorites.FavoritesRepository
import com.imn.iicnma.data.repository.movies.MovieRepository
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

    private var isFavored = false

    fun isFavoredStatus(movieId: Long) =
        favoritesRepository.isFavored(movieId)
            .onEach { isFavored = it }
            .asLiveData()

    fun loadMovie(id: Long) =
        movieRepository.getMovie(id).asLiveData(viewModelScope.coroutineContext)

    fun toggleFavorite(movieId: Long) = viewModelScope.launch {
        if (isFavored) {
            favoritesRepository.removeFromFavorites(movieId)
        } else {
            favoritesRepository.addToFavorites(movieId)
        }
    }
}