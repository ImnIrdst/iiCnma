package com.imn.iicnma.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.imn.iicnma.data.repository.MovieRepository
import com.imn.iicnma.model.Movie
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    @Assisted private val savedState: SavedStateHandle
) : ViewModel() {

    private val _movie = MutableLiveData<List<Movie>>().apply {
        viewModelScope.launch {
            value = movieRepository.getPopularMovies().results
        }

    }

    val movies: LiveData<List<Movie>> = _movie
}