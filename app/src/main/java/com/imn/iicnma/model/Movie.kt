package com.imn.iicnma.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("id") val id: Long,
    @SerializedName("title") val title: String,
    @SerializedName("vote_average") val rate: Float,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String, // TODO format date
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("popularity") val popularity: Float,
)