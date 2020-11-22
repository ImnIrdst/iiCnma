package com.imn.iicnma.data.local.search

import androidx.paging.PagingSource
import androidx.room.*
import com.imn.iicnma.data.local.common.CommonMovieListDao
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.remote.STARTING_PAGE_INDEX
import com.imn.iicnma.data.remote.model.MoviePagedListResponse
import com.imn.iicnma.data.repository.search.SearchLocalDataSource

@Dao
interface SearchDao : SearchLocalDataSource, CommonMovieListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(searchKey: List<SearchKeysEntity>)

    @Query("SELECT * FROM search_keys WHERE movieId = :movieId")
    override suspend fun getSearchKeysForMovieId(movieId: Long): SearchKeysEntity?

    @Query("DELETE FROM search_keys")
    suspend fun clearRemoteKeys()

    @Query(
        """SELECT * FROM movies WHERE
                title LIKE :queryString OR overview LIKE :queryString 
                ORDER BY popularity DESC, title ASC"""
    )
    override fun searchMovies(queryString: String): PagingSource<Int, MovieEntity>

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
        val keys = response.results.map { SearchKeysEntity(it.id, prevKey, nextKey) }
        insertAll(keys)
        insertAllMovies(response.toMovieEntityList())
    }
}