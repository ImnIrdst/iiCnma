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
import timber.log.Timber
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PopularMoviesPagerMediator(
    private val remote: PopularMoviesRemoteDataSource,
    private val local: PopularMoviesLocalDataSource,
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>,
    ): MediatorResult {
        Timber.v("loadType: $loadType, state: $state")
        val pageKey = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKey = getRemoteKeyClosestToCurrentPosition(state)
                remoteKey?.nextKey?.minus(1) ?: STARTING_PAGE_INDEX
            }
            LoadType.PREPEND -> {
                val remoteKey = getRemoteKeyForFirstItem(state)
                remoteKey?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
            LoadType.APPEND -> {
                val remoteKey = getRemoteKeyForLastItem(state)
                remoteKey?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKey != null)
            }
        }
        Timber.v("page: $pageKey")

        return try {
            val apiResponse = remote.getPopularMovies(pageKey)
            Timber.v("apiResponseResults ${apiResponse.results.size}")

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