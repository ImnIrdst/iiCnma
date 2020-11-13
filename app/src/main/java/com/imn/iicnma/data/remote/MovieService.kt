package com.imn.iicnma.data.remote

import com.imn.iicnma.data.repository.MovieRemoteDataSource
import com.imn.iicnma.model.MovieDetail
import com.imn.iicnma.model.MoviePagedList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieService : MovieRemoteDataSource {
    @GET("movie/{id}")
    suspend fun getMovie(@Path("id") id: Long): MovieDetail

    @GET("discover/movie?sort_by=popularity.desc")
    override suspend fun getPopularMovies(@Query("page") page: Int): MoviePagedList
}