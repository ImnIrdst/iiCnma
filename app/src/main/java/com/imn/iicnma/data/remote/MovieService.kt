package com.imn.iicnma.data.remote

import com.imn.iicnma.data.remote.model.MovieDetailResponse
import com.imn.iicnma.data.remote.model.MoviePagedListResponse
import com.imn.iicnma.data.repository.datasource.MovieRemoteDataSource
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService : MovieRemoteDataSource {
    @GET("movie/{id}")
    override suspend fun getMovie(@Path("id") id: Long): MovieDetailResponse

    @GET("discover/movie?sort_by=popularity.desc")
    override suspend fun getPopularMovies(@Query("page") page: Int): MoviePagedListResponse

    @GET("search/movie?sort_by=popularity.desc")
    override suspend fun searchMovies(
        @Query("query") query: String,
        @Query("page") page: Int
    ): MoviePagedListResponse
}