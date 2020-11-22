package com.imn.iicnma.data.repository.search

import androidx.paging.PagingSource
import androidx.room.Transaction
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.local.search.SearchKeysEntity
import com.imn.iicnma.data.remote.model.MoviePagedListResponse

interface SearchLocalDataSource {
    fun searchMovies(queryString: String): PagingSource<Int, MovieEntity>

    suspend fun getSearchKeysForMovieId(movieId: Long): SearchKeysEntity?

    @Transaction
    suspend fun cacheResponse(response: MoviePagedListResponse, pageKey: Int, isRefresh: Boolean)
}
