package com.imn.iicnma.data.repository.popular

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.bumptech.glide.load.HttpException
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.local.popular.PopularMovieKeysEntity
import com.imn.iicnma.data.remote.STARTING_PAGE_INDEX
import com.imn.iicnma.domain.model.utils.NetworkError
import com.imn.iicnma.domain.model.utils.UnknownError
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesPagerMediator(
    private val remote: PopularMoviesRemoteDataSource,
    private val local: PopularMoviesLocalDataSource,
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
    ): MediatorResult {
        val pageKey = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                    ?: throw InvalidObjectException("Remote key and the prevKey should not be null")
                remoteKey.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                    ?: throw InvalidObjectException("Remote key and the nextKey should not be null")
                remoteKey.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        return try {
            val apiResponse = remote.getPopularMovies(pageKey)
            local.cacheResponse(apiResponse, pageKey, loadType == LoadType.REFRESH)
            MediatorResult.Success(endOfPaginationReached = pageKey >= apiResponse.totalPages)
        } catch (exception: IOException) {
            MediatorResult.Error(UnknownError(exception))
        } catch (exception: HttpException) {
            MediatorResult.Error(NetworkError(exception))
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): PopularMovieKeysEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { movie -> local.getRemoteKeysForMovieId(movie.id) }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): PopularMovieKeysEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { movie -> local.getRemoteKeysForMovieId(movie.id) }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): PopularMovieKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)
                ?.id?.let { movieId -> local.getRemoteKeysForMovieId(movieId) }
        }
    }
}