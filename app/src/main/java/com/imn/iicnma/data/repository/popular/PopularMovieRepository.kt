package com.imn.iicnma.data.repository.popular

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import javax.inject.Inject

class PopularMovieRepository @Inject constructor(
    private val localDS: PopularMoviesLocalDataSource,
    private val remoteDS: PopularMoviesRemoteDataSource,
) {

    fun getPopularMovies() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false,
        ),
        remoteMediator = PopularMoviesPagerMediator(remoteDS, localDS),
        pagingSourceFactory = { localDS.getAll() }
    ).flow
}