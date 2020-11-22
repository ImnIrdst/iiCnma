package com.imn.iicnma.data.local.movie

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies: MovieEntity)

    @Query("SELECT * FROM movies ORDER BY page ASC, popularity DESC, title ASC")
    fun getAll(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies ORDER BY page ASC, popularity DESC, title ASC")
    fun getFavorites(): PagingSource<Int, MovieEntity>

    @Query("SELECT * FROM movies WHERE id=:id")
    fun getMovieFlow(id: Long): Flow<MovieEntity?>

    @Query("SELECT * FROM movies WHERE id=:id")
    suspend fun getMovie(id: Long): MovieEntity?

    @Query("DELETE FROM movies")
    suspend fun clearMovies()
}