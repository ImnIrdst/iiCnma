package com.imn.iicnma.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imn.iicnma.data.local.favorites.FavoritesDao
import com.imn.iicnma.data.local.favorites.FavoritesEntity
import com.imn.iicnma.data.local.movie.MovieDao
import com.imn.iicnma.data.local.movie.MovieEntity
import com.imn.iicnma.data.local.popular.PopularMovieKeysEntity
import com.imn.iicnma.data.local.popular.PopularMoviesDao
import com.imn.iicnma.data.local.search.SearchDao
import com.imn.iicnma.data.local.search.SearchKeysEntity

@Database(
    entities = [
        MovieEntity::class,
        SearchKeysEntity::class,
        FavoritesEntity::class,
        PopularMovieKeysEntity::class,
    ],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun moviesDao(): MovieDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun popularMoviesDao(): PopularMoviesDao
    abstract fun searchDao(): SearchDao
}