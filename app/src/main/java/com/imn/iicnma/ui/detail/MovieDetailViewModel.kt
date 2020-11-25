package com.imn.iicnma.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.imn.iicnma.data.repository.favorites.FavoritesRepository
import com.imn.iicnma.data.repository.movies.MovieRepository
import com.imn.iicnma.domain.model.utils.failureState
import com.imn.iicnma.domain.model.utils.loadingState
import com.imn.iicnma.domain.model.utils.successState
import com.imn.iicnma.domain.model.utils.toIIError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
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
        movieRepository.getMovie(id)
            .debounce(200)
            .mapLatest { successState(it) }
            .onStart { emit(loadingState()) }
            .catch { emit(failureState(it.toIIError())) }
            .asLiveData(viewModelScope.coroutineContext)

    fun toggleFavorite(movieId: Long) = viewModelScope.launch {
        if (isFavored) {
            favoritesRepository.removeFromFavorites(movieId)
        } else {
            favoritesRepository.addToFavorites(movieId)
        }
    }
}