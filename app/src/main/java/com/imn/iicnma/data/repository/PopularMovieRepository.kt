package com.imn.iicnma.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import com.imn.iicnma.data.repository.datasource.MovieRemoteDataSource
import com.imn.iicnma.data.repository.datasource.PopularMoviesLocalDataSource
import com.imn.iicnma.data.repository.mediator.PopularMoviesPagerMediator
import javax.inject.Inject

class PopularMovieRepository @Inject constructor(
    private val localDS: PopularMoviesLocalDataSource,
    private val remoteDS: MovieRemoteDataSource,
) {

    fun getPopularMovies() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        remoteMediator = PopularMoviesPagerMediator(remoteDS, localDS),
        pagingSourceFactory = { localDS.getAll() }
    ).flow
}