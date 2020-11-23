package com.imn.iicnma.data.repository.favorites

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.map
import com.imn.iicnma.data.local.favorites.FavoritesEntity
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val local: FavoritesLocalDataSource,
) {

    suspend fun addToFavorites(movieId: Long) {
        local.insert(FavoritesEntity(movieId = movieId))
    }

    suspend fun removeFromFavorites(movieId: Long) {
        local.delete(movieId)
    }

    fun getFavoriteMovies() = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { local.getAllFavoredMovies() }, // TODO test this on large data
        remoteMediator = null
    ).flow.map { pagingData ->
        pagingData.map { it.movieEntity }
    }

    fun isFavored(movieId: Long) = local.getFavoriteFlow(movieId).map { it != null }
}