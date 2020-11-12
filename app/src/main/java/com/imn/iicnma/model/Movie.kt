package com.imn.iicnma.model

import com.google.gson.annotations.SerializedName

data class Movie(
    @SerializedName("original_title") val originalTitle: String,
    @SerializedName("overview") val overview: String
)