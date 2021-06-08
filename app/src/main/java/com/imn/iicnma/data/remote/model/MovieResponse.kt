package com.imn.iicnma.data.remote.model

import com.google.gson.annotations.SerializedName
import com.imn.iicnma.data.local.movie.MovieEntity

data class MovieResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val rate: Float,
    @SerializedName("release_date") val releaseDate: String?, // TODO format date
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("genres") val genreResponses: List<GenreResponse>,
    @SerializedName("popularity") val popularity: Float,
) {

    fun toMovieEntity() = MovieEntity(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        rate = rate,
        posterPath = posterPath,
        popularity = popularity,
        genres = genreResponses.joinToString(", ")
    )
}

