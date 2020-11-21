package com.imn.iicnma.data.repository.datasource

import androidx.paging.PagingSource
import androidx.room.Query
import androidx.room.Transaction
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.local.search.PopularMovieKeysEntity
import com.imn.iicnma.data.remote.model.MoviePagedListResponse

interface PopularMoviesLocalDataSource {
    @Query(value = "SELECT * FROM movies ORDER BY page ASC, popularity DESC, title ASC")
    fun getAll(): PagingSource<Int, MovieEntity>

    @Query(value = "SELECT * FROM popular_movies_keys WHERE movieId = :id")
    suspend fun getRemoteKeysForMovieId(id: Long): PopularMovieKeysEntity?

    @Transaction
    suspend fun cacheResponse(response: MoviePagedListResponse, pageKey: Int, isRefresh: Boolean)
}
