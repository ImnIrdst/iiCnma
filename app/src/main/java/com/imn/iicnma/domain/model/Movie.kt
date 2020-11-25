package com.imn.iicnma.domain.model

data class Movie(
    val id: Long,
    val title: String,
    val overview: String,
    val genres: String?,
    val rate100: Int,
    val releaseDate: String,
    val posterUrl: String?,
    val popularity: Float,
)