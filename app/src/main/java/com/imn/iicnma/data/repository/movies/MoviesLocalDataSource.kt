package com.imn.iicnma.data.repository.movies

import com.imn.iicnma.data.local.movie.MovieEntity
import kotlinx.coroutines.flow.Flow

interface MoviesLocalDataSource {
    fun getMovieFlow(id: Long): Flow<MovieEntity?>
    suspend fun insert(movies: MovieEntity)
}
