package com.imn.iicnma

import com.google.gson.Gson
import com.imn.iicnma.data.remote.model.MoviePagedListResponse
import com.imn.iicnma.data.remote.model.MovieResponse
import java.net.UnknownHostException

val unknownHostException = UnknownHostException()

val pagedListResponse: MoviePagedListResponse =
    Gson().fromJson(pagedListJson, MoviePagedListResponse::class.java)
val movieEntityList = pagedListResponse.toMovieEntityList()

val movieItemEntity = movieEntityList[0]
val movieItem = movieItemEntity.toMovie()


val movieDetailResponse: MovieResponse = Gson().fromJson(movieDetailJson, MovieResponse::class.java)
val movieDetailEntity = movieDetailResponse.toMovieEntity()
val movieDetail = movieDetailEntity.toMovie()
val movieId = movieDetailResponse.id

