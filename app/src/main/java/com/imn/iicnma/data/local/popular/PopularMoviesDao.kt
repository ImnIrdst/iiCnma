package com.imn.iicnma.data.local.popular

import androidx.paging.PagingSource
import androidx.room.*
import com.imn.iicnma.data.local.common.CommonMovieListDao
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.remote.STARTING_PAGE_INDEX
import com.imn.iicnma.data.remote.model.MoviePagedListResponse
import com.imn.iicnma.data.repository.popular.PopularMoviesLocalDataSource

@Dao
interface PopularMoviesDao : CommonMovieListDao, PopularMoviesLocalDataSource {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<PopularMovieKeysEntity>)

    @Query("SELECT * FROM popular_movies_keys WHERE movieId = :id")
    override suspend fun getRemoteKeysForMovieId(id: Long): PopularMovieKeysEntity?

    @Query("DELETE FROM popular_movies_keys")
    suspend fun clearRemoteKeys()

    @Query(
        """SELECT movies.* FROM movies
        INNER JOIN popular_movies_keys ON movies.id=popular_movies_keys.movieId
        ORDER BY curKey ASC, popularity DESC, title ASC"""
    )
    override fun getAll(): PagingSource<Int, MovieEntity>

    @Transaction
    override suspend fun cacheResponse(
        response: MoviePagedListResponse,
        pageKey: Int,
        isRefresh: Boolean,
    ) {
        if (isRefresh) {
            clearRemoteKeys()
        }
        val prevKey = if (pageKey == STARTING_PAGE_INDEX) null else pageKey - 1
        val nextKey = if (pageKey >= response.totalPages) null else pageKey + 1
        val keys = response.results.map { PopularMovieKeysEntity(it.id, prevKey, pageKey, nextKey) }
        insertAll(keys)
        insertAllMovies(response.toMovieEntityList())
    }
}