package com.imn.iicnma.data.repository.search

import androidx.paging.*
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import com.imn.iicnma.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val local: SearchLocalDataSource,
    private val remote: SearchRemoteDataSource,
) {

    @OptIn(ExperimentalPagingApi::class)
    fun search(query: String): Flow<PagingData<Movie>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { local.searchMovies(dbQuery) }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = SearchPagerMediator(query, remote, local),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }
    }
}
