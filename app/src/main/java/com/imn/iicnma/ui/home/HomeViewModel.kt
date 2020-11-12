package com.imn.iicnma.ui.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.imn.iicnma.data.repository.MovieRepository

class HomeViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    @Assisted private val savedState: SavedStateHandle
) : ViewModel() {

    val movies = movieRepository.getPopularMovies().cachedIn(viewModelScope)
}