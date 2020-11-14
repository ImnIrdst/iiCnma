package com.imn.iicnma.data.local.favorites

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movies: FavoritesEntity)

    @Query("DELETE FROM favorites WHERE id=:movieId")
    suspend fun delete(movieId: Long)

    @Query("SELECT * FROM favorites WHERE id=:id")
    fun getMovie(id: Long): Flow<FavoritesEntity?>

    @Query("SELECT * FROM favorites")
    fun getAll(): PagingSource<Int, FavoritesEntity>
}