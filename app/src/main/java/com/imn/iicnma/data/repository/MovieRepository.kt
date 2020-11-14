package com.imn.iicnma.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.imn.iicnma.data.local.MovieDatabase
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.remote.MovieService
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

    fun search(query: String): Flow<PagingData<MovieEntity>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { movieDatabase.moviesDao().searchMovies(dbQuery) }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = SearchMoviePagerMediator(query, movieService, movieDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

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

    suspend fun addToFavorites(movieId: Long) {
        movieDatabase.moviesDao().getMovie(movieId)?.let {
            movieDatabase.favoritesDao().insert(it.toFavoriteEntity())
        }
    }

    suspend fun removeFromFavorites(movieId: Long) {
        movieDatabase.favoritesDao().delete(movieId)
    }

    fun getFavoriteMovies() = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
        pagingSourceFactory = { movieDatabase.favoritesDao().getAll() },
        remoteMediator = null
    ).flow

    fun isFavored(movieId: Long) = movieDatabase.favoritesDao().getMovie(movieId).map { it != null }
}