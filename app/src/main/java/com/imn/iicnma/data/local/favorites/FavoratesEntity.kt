package com.imn.iicnma.data.local.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.imn.iicnma.data.remote.CDN_BASE_URL

@Entity(tableName = "favorites")
data class FavoritesEntity(
    @PrimaryKey @field:SerializedName("id") val id: Long,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("genres") val genres: String?,
    @field:SerializedName("vote_average") val rate: Float,
    @field:SerializedName("release_date") val releaseDate: String,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("popularity") val popularity: Float,
    @field:SerializedName("page") val page: Int, // this is used because of api bug
) {
    val posterUrl: String
        get() = CDN_BASE_URL + posterPath
}