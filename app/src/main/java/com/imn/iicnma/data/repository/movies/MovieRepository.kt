package com.imn.iicnma.data.repository.movies

import android.util.Log
import com.imn.iicnma.data.local.movie.MovieEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import retrofit2.HttpException
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val local: MoviesLocalDataSource,
    private val remote: MovieRemoteDataSource,
) {

    fun getMovie(id: Long): Flow<MovieEntity?> =
        local.getMovieFlow(id).transform { localMovie ->

            emit(localMovie)

            try { // TODO better error handling
                if (localMovie == null || !localMovie.isDetailLoaded()) {
                    var movieEntity = remote.getMovie(id).toMovieEntity()
                    if (localMovie != null) movieEntity = movieEntity.copy(page = localMovie.page)
                    local.insert(movieEntity)
                }
            } catch (e: HttpException) {
                Log.e("MovieRepository", "error happened when loading movie details", e)
            }
        }
}