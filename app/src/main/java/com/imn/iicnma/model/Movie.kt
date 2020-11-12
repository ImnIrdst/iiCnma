package com.imn.iicnma.model

import com.google.gson.annotations.SerializedName
import com.imn.iicnma.data.remote.CDN_BASE_URL

data class Movie(
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String,
    @SerializedName("release_date") val releaseDate: String, // TODO format date
    @SerializedName("poster_path") private val posterPath: String
) {
    val posterUrl: String
        get() = CDN_BASE_URL + posterPath
}