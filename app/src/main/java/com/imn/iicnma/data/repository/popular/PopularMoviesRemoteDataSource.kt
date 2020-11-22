package com.imn.iicnma.data.repository.popular

import com.imn.iicnma.data.remote.model.MoviePagedListResponse

interface PopularMoviesRemoteDataSource {
    suspend fun getPopularMovies(page: Int): MoviePagedListResponse
}
