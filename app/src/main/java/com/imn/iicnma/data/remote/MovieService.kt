package com.imn.iicnma.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class Movie(
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String
)

interface MovieService {
    @GET("movie/550")
    suspend fun getMovie(): Movie
}