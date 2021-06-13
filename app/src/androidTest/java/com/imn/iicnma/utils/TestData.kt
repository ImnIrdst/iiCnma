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
val page1Response: MoviePagedListResponse =
    Gson().fromJson(page1Json, MoviePagedListResponse::class.java)
val page2Response: MoviePagedListResponse =
    Gson().fromJson(page2Json, MoviePagedListResponse::class.java)

val movieItemEntity = page1Response.toMovieEntityList()[0]
val movieItemEntity2 = page2Response.toMovieEntityList()[0]
val movieItem = movieItemEntity.toMovie()

val favoriteItemEntity = FavoritesEntity(movieItemEntity.id)
val favoriteItemEntity2 = FavoritesEntity(movieItemEntity2.id)

val favoriteRelation = FavoriteMovieRelation(movieItemEntity, favoriteItemEntity)
val favoriteRelation2 = FavoriteMovieRelation(movieItemEntity2, favoriteItemEntity2)

//val popularMovie = PopularMovieKeysEntity(movieItemEntity.id, null, 1, 2)
//val popularMovie2 = PopularMovieKeysEntity(movieItemEntity2.id, 1, 2, null)

val movieDetailResponse: MovieResponse = Gson().fromJson(movieDetailJson, MovieResponse::class.java)
val movieDetailEntity = movieDetailResponse.toMovieEntity()
val movieDetail = movieDetailEntity.toMovie()
val movieId = movieDetailResponse.id

const val LATCH_AWAIT_TIMEOUT = 1L