package com.imn.iicnma.data.remote.model

import com.google.gson.annotations.SerializedName
import com.imn.iicnma.data.local.movie.MovieEntity

data class MoviePagedListResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<Movie>,
) {
    fun toMovieEntityList() = results.map {
        MovieEntity(
            id = it.id,
            title = it.title,
            overview = it.overview,
            rate = it.rate,
            genres = null,
            releaseDate = it.releaseDate,
            posterPath = it.posterPath,
            popularity = it.popularity,
            page = page
        )
    }
}

data class Movie(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("vote_average") val rate: Float,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String, // TODO format date
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("popularity") val popularity: Float,
)