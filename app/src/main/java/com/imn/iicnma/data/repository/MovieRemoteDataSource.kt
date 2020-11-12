package com.imn.iicnma.data.repository

import com.imn.iicnma.model.MoviePagedList

interface MovieRemoteDataSource {
    suspend fun getPopularMovies(page: Int): MoviePagedList
}