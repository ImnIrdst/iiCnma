package com.imn.iicnma.data.remote.popular

import com.imn.iicnma.data.remote.model.MoviePagedListResponse
import com.imn.iicnma.data.repository.popular.PopularMoviesRemoteDataSource
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularMoviesService : PopularMoviesRemoteDataSource {

    @GET("discover/movie?sort_by=popularity.desc")
    override suspend fun getPopularMovies(@Query("page") page: Int): MoviePagedListResponse
}