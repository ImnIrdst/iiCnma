package com.imn.iicnma.data.repository.search

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val local: SearchLocalDataSource,
    private val remote: SearchRemoteDataSource,
) {

    fun search(query: String): Flow<PagingData<MovieEntity>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { local.searchMovies(dbQuery) }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = SearchPagerMediator(query, remote, local),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }
}
