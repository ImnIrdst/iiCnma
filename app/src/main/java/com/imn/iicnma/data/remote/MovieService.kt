package com.imn.iicnma.data.remote

import com.imn.iicnma.data.remote.model.MovieResponse
import com.imn.iicnma.data.repository.movies.MovieRemoteDataSource
import retrofit2.http.GET
import retrofit2.http.Path

interface MovieService : MovieRemoteDataSource {

    @GET("movie/{id}")
    override suspend fun getMovie(@Path("id") id: Long): MovieResponse
}