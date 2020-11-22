package com.imn.iicnma.data.repository.movies

import com.imn.iicnma.data.remote.model.MovieDetailResponse

interface MovieRemoteDataSource {
    suspend fun getMovie(id: Long): MovieDetailResponse
}