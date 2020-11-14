package com.imn.iicnma.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imn.iicnma.data.local.favorites.FavoritesDao
import com.imn.iicnma.data.local.favorites.FavoritesEntity
import com.imn.iicnma.data.local.keys.RemoteKeysDao
import com.imn.iicnma.data.local.keys.RemoteKeysEntity
import com.imn.iicnma.data.local.movie.MovieDao
import com.imn.iicnma.data.local.movie.MovieEntity

@Database(
    entities = [
        MovieEntity::class,
        RemoteKeysEntity::class,
        FavoritesEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun moviesDao(): MovieDao
    abstract fun favoritesDao(): FavoritesDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}