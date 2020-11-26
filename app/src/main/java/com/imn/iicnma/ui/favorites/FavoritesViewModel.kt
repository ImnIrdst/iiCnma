package com.imn.iicnma.ui.favorites

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.imn.iicnma.data.repository.favorites.FavoritesRepository
import kotlinx.coroutines.flow.debounce

class FavoritesViewModel @ViewModelInject constructor(
    favoritesRepository: FavoritesRepository,
) : ViewModel() {

    val movies = favoritesRepository.getFavoriteMovies().debounce(500)
}