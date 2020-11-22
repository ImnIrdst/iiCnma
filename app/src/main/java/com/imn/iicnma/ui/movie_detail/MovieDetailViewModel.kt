package com.imn.iicnma.ui.movie_detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.imn.iicnma.data.repository.movies.MovieRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private var isFavored = false

    fun isFavoredStatus(movieId: Long) =
        movieRepository.isFavored(movieId).map {
            isFavored = it
            it
        }.asLiveData()

    fun loadMovie(id: Long) =
        movieRepository.getMovie(id).asLiveData(viewModelScope.coroutineContext)

    fun toggleFavorite(movieId: Long) = viewModelScope.launch {
        if (isFavored) {
            movieRepository.removeFromFavorites(movieId)
        } else {
            movieRepository.addToFavorites(movieId)
        }
    }
}