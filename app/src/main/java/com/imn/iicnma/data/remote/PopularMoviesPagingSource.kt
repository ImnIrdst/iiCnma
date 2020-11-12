package com.imn.iicnma.data.remote

import androidx.paging.PagingSource
import com.imn.iicnma.model.Movie
import retrofit2.HttpException
import java.io.IOException

class PopularMoviesPagingSource constructor(
    private val service: MovieService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.getPopularMovies(page)
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page >= response.totalPages) null else page + 1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}