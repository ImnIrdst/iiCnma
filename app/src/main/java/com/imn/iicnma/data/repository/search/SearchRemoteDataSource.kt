package com.imn.iicnma.data.repository.search

import com.imn.iicnma.data.remote.model.MoviePagedListResponse

interface SearchRemoteDataSource {
    suspend fun searchMovies(query: String, page: Int): MoviePagedListResponse
}
