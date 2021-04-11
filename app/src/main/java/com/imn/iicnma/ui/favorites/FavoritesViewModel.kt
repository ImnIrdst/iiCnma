package com.imn.iicnma.ui.favorites

import androidx.lifecycle.ViewModel
import com.imn.iicnma.data.repository.favorites.FavoritesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    favoritesRepository: FavoritesRepository,
) : ViewModel() {

    val movies = favoritesRepository.getFavoriteMovies()
}