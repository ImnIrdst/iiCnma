package com.imn.iicnma.ui.movie_detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.repository.MovieRepository
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movie = MutableLiveData<MovieEntity>()
    val movie: LiveData<MovieEntity> = _movie

    private var isFavored = false

    fun isFavoredStatus(movieId: Long) =
        movieRepository.isFavored(movieId).map {
            println("imnimn isFavored $isFavored")
            isFavored = it
            it
        }.asLiveData()

    fun loadMovie(id: Long) = viewModelScope.launch {
        _movie.postValue(movieRepository.getMovie(id)) // TODO handle errors
    }

    fun toggleFavorite(movieId: Long) = viewModelScope.launch {
        if (isFavored) {
            movieRepository.removeFromFavorites(movieId)
        } else {
            movieRepository.addToFavorites(movieId)
        }
    }
}