package com.imn.iicnma.data.local.movie

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Query("SELECT * FROM movies ORDER BY popularity DESC, originalTitle ASC")
    fun getAll(): PagingSource<Int, MovieEntity>

    @Query("DELETE FROM movies")
    suspend fun clearMovies()
}