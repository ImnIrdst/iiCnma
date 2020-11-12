package com.imn.iicnma.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

data class Movie(
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String
)

interface MovieService {
    @GET("movie/550?api_key=1f058b502c1b3371e7e38ea1e151e007")
    suspend fun getMovie(): Movie
}