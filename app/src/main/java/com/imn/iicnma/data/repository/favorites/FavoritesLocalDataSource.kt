package com.imn.iicnma.data.repository.favorites

import androidx.paging.PagingSource
import com.imn.iicnma.data.local.favorites.FavoriteMovieRelation
import com.imn.iicnma.data.local.favorites.FavoritesEntity
import kotlinx.coroutines.flow.Flow

interface FavoritesLocalDataSource {
    suspend fun insert(favorite: FavoritesEntity)
    suspend fun delete(movieId: Long)
    fun getFavoriteFlow(id: Long): Flow<FavoritesEntity?>
    fun getAllFavoredMovies(): PagingSource<Int, FavoriteMovieRelation>
}
