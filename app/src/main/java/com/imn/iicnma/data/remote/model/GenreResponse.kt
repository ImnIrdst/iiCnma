package com.imn.iicnma.data.remote.model

import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("name") val name: String,
) {
    override fun toString() = name
}