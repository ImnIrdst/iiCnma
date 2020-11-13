package com.imn.iicnma.model

import com.google.gson.annotations.SerializedName
import com.imn.iicnma.data.local.movie.MovieEntity

data class MoviePagedList(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<Movie>,
) {
    fun toMovieEntityList() = results.map {
        MovieEntity(
            id = it.id,
            title = it.title,
            overview = null,
            genres = null,
            releaseDate = it.releaseDate,
            posterPath = it.posterPath,
            popularity = it.popularity,
            page = page
        )
    }
}