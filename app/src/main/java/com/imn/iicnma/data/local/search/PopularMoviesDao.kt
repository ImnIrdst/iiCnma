package com.imn.iicnma.data.local.search

import androidx.paging.PagingSource
import androidx.room.*
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.remote.STARTING_PAGE_INDEX
import com.imn.iicnma.data.remote.model.MoviePagedListResponse
import com.imn.iicnma.data.repository.popular.PopularMoviesLocalDataSource

@Dao
interface PopularMoviesDao: PopularMoviesLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PopularMovieKeysEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllMovies(remoteKey: List<MovieEntity>)

    @Query("SELECT * FROM popular_movies_keys WHERE movieId = :id")
    override suspend fun getRemoteKeysForMovieId(id: Long): PopularMovieKeysEntity?

    @Query("DELETE FROM popular_movies_keys")
    suspend fun clearRemoteKeys()

    @Query("SELECT * FROM movies ORDER BY page ASC, popularity DESC, title ASC")
    override fun getAll(): PagingSource<Int, MovieEntity> // TODO maybe you can use inheritance for these

    @Transaction
    override suspend fun cacheResponse(response: MoviePagedListResponse, pageKey: Int, isRefresh: Boolean) {
        if (isRefresh) {
            clearRemoteKeys()
        }
        val prevKey = if (pageKey == STARTING_PAGE_INDEX) null else pageKey - 1
        val nextKey = if (pageKey >= response.totalPages) null else pageKey + 1
        val keys = response.results.map { PopularMovieKeysEntity(it.id, prevKey, nextKey) }
        insertAll(keys)
        insertAllMovies(response.toMovieEntityList())
    }
}