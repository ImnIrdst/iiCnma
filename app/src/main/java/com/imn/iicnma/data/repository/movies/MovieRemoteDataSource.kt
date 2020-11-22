package com.imn.iicnma.data.repository.movies

import com.imn.iicnma.data.remote.model.MovieDetailResponse
import com.imn.iicnma.data.remote.model.MoviePagedListResponse

interface MovieRemoteDataSource {
    suspend fun getMovie(id: Long): MovieDetailResponse
    suspend fun getPopularMovies(page: Int): MoviePagedListResponse
    suspend fun searchMovies(query: String, page: Int): MoviePagedListResponse
}