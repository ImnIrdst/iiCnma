package com.imn.iicnma.data.local.favorites

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.repository.favorites.FavoritesLocalDataSource
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao : FavoritesLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(movies: FavoritesEntity)

    @Query("DELETE FROM favorites WHERE id=:movieId")
    override suspend fun delete(movieId: Long)

    @Query("SELECT * FROM favorites WHERE id=:id")
    override fun getMovieFlow(id: Long): Flow<FavoritesEntity?>

    @Query("SELECT * FROM movies WHERE id=:id")
    override suspend fun getMovie(id: Long): MovieEntity?

    @Query("SELECT * FROM favorites")
    override fun getAll(): PagingSource<Int, FavoritesEntity>
}