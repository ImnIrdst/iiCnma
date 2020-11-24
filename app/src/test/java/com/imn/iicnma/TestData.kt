package com.imn.iicnma

import com.google.gson.Gson
import com.imn.iicnma.data.remote.model.MoviePagedListResponse
import com.imn.iicnma.data.remote.model.MovieResponse

val httpException = Throwable("404")

val pagedListResponse = Gson().fromJson(pagedListJson, MoviePagedListResponse::class.java)
val movieEntityList = pagedListResponse.toMovieEntityList()

val movieItemEntity = movieEntityList[0]

val movieDetailResponse = Gson().fromJson(movieDetailJson, MovieResponse::class.java)
val movieDetailEntity = movieDetailResponse.toMovieEntity()
val movieId = movieDetailResponse.id

