package com.imn.iicnma.data.repository.popular

import androidx.paging.PagingSource
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.local.popular.PopularMovieKeysEntity
import com.imn.iicnma.data.remote.model.MoviePagedListResponse

interface PopularMoviesLocalDataSource {
    fun getAll(): PagingSource<Int, MovieEntity>
    suspend fun getRemoteKeysForMovieId(id: Long): PopularMovieKeysEntity?
    suspend fun cacheResponse(response: MoviePagedListResponse, pageKey: Int, isRefresh: Boolean)
}
