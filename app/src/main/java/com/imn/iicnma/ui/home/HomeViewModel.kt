package com.imn.iicnma.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.imn.iicnma.data.repository.popular.PopularMovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import javax.inject.Inject


@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: PopularMovieRepository,
) : ViewModel() {

    val movies = repository.getPopularMovies().cachedIn(viewModelScope)
}