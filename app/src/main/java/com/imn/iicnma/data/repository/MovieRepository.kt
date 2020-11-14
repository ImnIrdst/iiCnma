package com.imn.iicnma.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.imn.iicnma.data.local.MovieDatabase
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.remote.MovieService
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieService: MovieService,
    private val movieDatabase: MovieDatabase,
) {
    fun getPopularMovies() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        remoteMediator = MoviePagerMediator(movieService, movieDatabase),
        pagingSourceFactory = { movieDatabase.moviesDao().getAll() }
    ).flow

    suspend fun getMovie(id: Long): MovieEntity {
        val localMovie = movieDatabase.moviesDao().getMovie(id)
        return if (localMovie?.isDetailLoaded() == true) {
            localMovie
        } else {
            var movieEntity = movieService.getMovie(id).toMovieEntity()
            if (localMovie != null) movieEntity = movieEntity.copy(page = localMovie.page)
            movieDatabase.moviesDao().insert(movieEntity)
            movieEntity
        }
    }
}