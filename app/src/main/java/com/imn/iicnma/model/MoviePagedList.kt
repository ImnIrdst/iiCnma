package com.imn.iicnma.model

import com.google.gson.annotations.SerializedName

data class MoviePagedList(
    @SerializedName("page") val page: Int,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("results") val results: List<Movie>,
)