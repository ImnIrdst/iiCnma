package com.imn.iicnma.data.repository.favorites

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepository @Inject constructor(
    private val local: FavoritesLocalDataSource,
) {

    suspend fun addToFavorites(movieId: Long) {
        local.getMovie(movieId)?.let {
            local.insert(it.toFavoriteEntity())
        }
    }

    suspend fun removeFromFavorites(movieId: Long) {
        local.delete(movieId)
    }

    fun getFavoriteMovies() = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { local.getAll() }, // TODO test this
        remoteMediator = null
    ).flow

    fun isFavored(movieId: Long) = local.getMovieFlow(movieId).map { it != null }
}