package com.imn.iicnma.data.remote.model

import com.google.gson.annotations.SerializedName
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.remote.CDN_BASE_URL

data class MovieDetailResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("vote_average") val rate: Float,
    @SerializedName("release_date") val releaseDate: String, // TODO format date
    @SerializedName("poster_path") val posterPath: String,
    @SerializedName("genres") val genres: List<Genre>,
    @SerializedName("popularity") val popularity: Float,
) {
    val posterUrl: String
        get() = CDN_BASE_URL + posterPath

    fun toMovieEntity() = MovieEntity(
        id = id,
        title = title,
        overview = overview,
        releaseDate = releaseDate,
        rate = rate,
        posterPath = posterPath,
        popularity = popularity,
        genres = genres.joinToString(", "),
        page = -1,
    )
}

data class Genre(
    @SerializedName("name") val name: String,
) {
    override fun toString() = name
}