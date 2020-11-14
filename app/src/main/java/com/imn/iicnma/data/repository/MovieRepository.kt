package com.imn.iicnma.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.imn.iicnma.data.local.MovieDatabase
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import com.imn.iicnma.data.repository.datasource.MovieRemoteDataSource
import com.imn.iicnma.data.repository.mediator.MoviePagerMediator
import com.imn.iicnma.data.repository.mediator.SearchMoviePagerMediator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieDatabase: MovieDatabase,
    private val movieRemoteDataSource: MovieRemoteDataSource,
) {

    private val movieDetailsScope = CoroutineScope(Dispatchers.IO)

    fun getPopularMovies() = Pager(
        config = PagingConfig(
            pageSize = NETWORK_PAGE_SIZE,
            enablePlaceholders = false
        ),
        remoteMediator = MoviePagerMediator(movieRemoteDataSource, movieDatabase),
        pagingSourceFactory = { movieDatabase.moviesDao().getAll() }
    ).flow

    fun search(query: String): Flow<PagingData<MovieEntity>> {
        val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { movieDatabase.moviesDao().searchMovies(dbQuery) }

        return Pager(
            config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = SearchMoviePagerMediator(query, movieRemoteDataSource, movieDatabase),
            pagingSourceFactory = pagingSourceFactory
        ).flow
    }

    fun getMovie(id: Long): Flow<MovieEntity?> =
        movieDatabase.moviesDao().getMovieFlow(id).map { localMovie ->
            movieDetailsScope.launch {
                try {
                    if (localMovie == null || !localMovie.isDetailLoaded()) {
                        var movieEntity = movieRemoteDataSource.getMovie(id).toMovieEntity()
                        if (localMovie != null) movieEntity =
                            movieEntity.copy(page = localMovie.page)
                        movieDatabase.moviesDao().insert(movieEntity)
                    }
                } catch (e: HttpException) {
                    Log.e("MovieRepository", "error happened when loading movie details", e)
                }
            }
            localMovie
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

    fun cancelMovieDetailsScope() {
        movieDetailsScope.coroutineContext.cancel()
    }
}