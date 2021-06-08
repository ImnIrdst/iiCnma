package com.imn.iicnma.data.local.movie

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.imn.iicnma.data.remote.CDN_BASE_URL
import com.imn.iicnma.domain.model.Movie

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("title") val title: String,
    @field:SerializedName("overview") val overview: String,
    @field:SerializedName("genres") val genres: String?,
    @field:SerializedName("vote_average") val rate: Float,
    @field:SerializedName("release_date") val releaseDate: String?,
    @field:SerializedName("poster_path") val posterPath: String?,
    @field:SerializedName("popularity") val popularity: Float,
) {

    fun isDetailLoaded() = genres != null

    fun toMovie() = Movie(
        id = id,
        title = title,
        overview = overview,
        genres = genres,
        rate100 = (rate * 10).toInt(),
        releaseDate = releaseDate,
        posterUrl = CDN_BASE_URL + posterPath,
        popularity = popularity,
    )
}