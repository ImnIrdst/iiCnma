package com.imn.iicnma.ui.movie_detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.repository.MovieRepository
import kotlinx.coroutines.launch

class MovieDetailViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    private val _movie = MutableLiveData<MovieEntity>()
    val movie: LiveData<MovieEntity> = _movie

    fun loadMovie(id: Long) = viewModelScope.launch {
        _movie.postValue(movieRepository.getMovie(id)) // TODO handle errors
    }
}