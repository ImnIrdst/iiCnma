package com.imn.iicnma.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.imn.iicnma.data.remote.MovieService
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import com.imn.iicnma.data.remote.PopularMoviesPagingSource
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService
) {
    fun getPopularMovies() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        pagingSourceFactory = { PopularMoviesPagingSource(movieService) }

    ).flow
}