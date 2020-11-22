package com.imn.iicnma.data.repository.movies

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.imn.iicnma.data.local.MovieDatabase
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.remote.NETWORK_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import retrofit2.HttpException
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val movieDatabase: MovieDatabase,
    private val movieRemoteDataSource: MovieRemoteDataSource,
) {

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
        movieDatabase.moviesDao().getMovieFlow(id).transform { localMovie ->
            emit(localMovie)
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