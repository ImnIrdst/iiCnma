package com.imn.iicnma.data.local.favorites

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "favorites")
data class FavoritesEntity(
    @PrimaryKey
    @field:SerializedName("movieId") val movieId: Long,
)