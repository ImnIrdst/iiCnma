package com.imn.iicnma.data.local.favorites

import androidx.room.Embedded
import androidx.room.Relation
import com.imn.iicnma.data.local.movie.MovieEntity

data class FavoriteMovieRelation(
    @Embedded
    val movieEntity: MovieEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "movieId"
    )
    val favoritesEntity: FavoritesEntity,
)