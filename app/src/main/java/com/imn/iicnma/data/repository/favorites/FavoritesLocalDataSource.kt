package com.imn.iicnma.data.repository.favorites

import androidx.paging.PagingSource
import com.imn.iicnma.data.local.favorites.FavoritesEntity
import com.imn.iicnma.data.local.movie.MovieEntity
import kotlinx.coroutines.flow.Flow

interface FavoritesLocalDataSource {
    suspend fun insert(movies: FavoritesEntity)
    suspend fun delete(movieId: Long)
    suspend fun getMovie(id: Long): MovieEntity?
    fun getMovieFlow(id: Long): Flow<FavoritesEntity?>
    fun getAll(): PagingSource<Int, FavoritesEntity>
}
