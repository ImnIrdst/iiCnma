package com.imn.iicnma.ui.search

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class SearchViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
) : ViewModel() {

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<MovieEntity>>? = null

    fun search(query: String): Flow<PagingData<MovieEntity>> {
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }

        val newResult = movieRepository.search(query).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}