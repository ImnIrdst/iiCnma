package com.imn.iicnma.ui.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.imn.iicnma.data.repository.popular.PopularMovieRepository
import kotlinx.coroutines.FlowPreview


@OptIn(FlowPreview::class)
class HomeViewModel @ViewModelInject constructor(
    repository: PopularMovieRepository,
) : ViewModel() {

    val movies = repository.getPopularMovies().cachedIn(viewModelScope)
}