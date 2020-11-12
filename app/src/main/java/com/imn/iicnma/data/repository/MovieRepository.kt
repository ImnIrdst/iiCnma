package com.imn.iicnma.data.repository

import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val remoteDataSource: MovieRemoteDataSource
) {
    suspend fun getPopularMovies() = remoteDataSource.getPopularMovies(1)
}