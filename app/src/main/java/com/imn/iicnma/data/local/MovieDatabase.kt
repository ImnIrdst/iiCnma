package com.imn.iicnma.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.imn.iicnma.data.local.keys.RemoteKeysDao
import com.imn.iicnma.data.local.keys.RemoteKeysEntity
import com.imn.iicnma.data.local.movie.MovieDao
import com.imn.iicnma.data.local.movie.MovieEntity

@Database(
    entities = [
        MovieEntity::class,
        RemoteKeysEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun moviesDao(): MovieDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}