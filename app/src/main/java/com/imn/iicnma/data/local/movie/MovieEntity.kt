package com.imn.iicnma.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.imn.iicnma.data.remote.CDN_BASE_URL

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey @field:SerializedName("id") val id: Long,
    @field:SerializedName("original_title") val originalTitle: String,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("release_date") val releaseDate: String,
    @field:SerializedName("poster_path") val posterPath: String,
    @field:SerializedName("popularity") val popularity: Float,
) {
    val posterUrl: String
        get() = CDN_BASE_URL + posterPath
}