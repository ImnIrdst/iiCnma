package com.imn.iicnma.utils

import com.google.gson.Gson
import com.imn.iicnma.data.local.favorites.FavoriteMovieRelation
import com.imn.iicnma.data.local.favorites.FavoritesEntity
import com.imn.iicnma.data.remote.model.MoviePagedListResponse
import com.imn.iicnma.data.remote.model.MovieResponse
import java.net.UnknownHostException

val unknownHostException = UnknownHostException()

val pagedListResponse: MoviePagedListResponse =
    Gson().fromJson(pagedListJson, MoviePagedListResponse::class.java)
val movieEntityList = pagedListResponse.toMovieEntityList()

val movieItemEntity = movieEntityList[0]
val movieItemEntity2 = movieEntityList[1]
val movieItem = movieItemEntity.toMovie()

val favoriteItemEntity = FavoritesEntity(movieItemEntity.id)
val favoriteItemEntity2 = FavoritesEntity(movieItemEntity2.id)

val favoriteRelation = FavoriteMovieRelation(movieItemEntity, favoriteItemEntity)
val favoriteRelation2 = FavoriteMovieRelation(movieItemEntity2, favoriteItemEntity2)

val movieDetailResponse: MovieResponse = Gson().fromJson(movieDetailJson, MovieResponse::class.java)
val movieDetailEntity = movieDetailResponse.toMovieEntity()
val movieDetail = movieDetailEntity.toMovie()
val movieId = movieDetailResponse.id

const val LATCH_AWAIT_TIMEOUT = 1L