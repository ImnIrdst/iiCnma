package com.imn.iicnma.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.imn.iicnma.data.repository.MovieRepository

class SearchViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    val movies = movieRepository.getPopularMovies().cachedIn(viewModelScope)
}