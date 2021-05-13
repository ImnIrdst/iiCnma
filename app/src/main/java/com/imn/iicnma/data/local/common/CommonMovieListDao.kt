package com.imn.iicnma.data.local.common

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.imn.iicnma.data.local.movie.MovieEntity

interface CommonMovieListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(movies: List<MovieEntity>)
}