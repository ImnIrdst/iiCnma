package com.imn.iicnma.data.local.favorites

import androidx.paging.PagingSource
import androidx.room.*
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.repository.favorites.FavoritesLocalDataSource
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao : FavoritesLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    override suspend fun insert(favorite: FavoritesEntity)

    @Query("DELETE FROM favorites WHERE movieId=:movieId")
    override suspend fun delete(movieId: Long)

    @Query("SELECT * FROM favorites WHERE movieId=:id")
    override fun getFavoriteFlow(id: Long): Flow<FavoritesEntity?>

    @Query("SELECT * FROM movies WHERE id=:id")
    override suspend fun getMovie(id: Long): MovieEntity?

    @Transaction
    @Query("SELECT movies.* FROM movies INNER JOIN favorites ON movies.id=favorites.movieId")
    override fun getAllFavoredMovies(): PagingSource<Int, FavoriteMovieRelation>
}