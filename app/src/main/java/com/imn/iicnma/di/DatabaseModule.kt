package com.imn.iicnma.di

import android.content.Context
import androidx.room.Room
import com.imn.iicnma.data.local.MovieDatabase
import com.imn.iicnma.data.local.keys.RemoteKeysDao
import com.imn.iicnma.data.local.movie.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): MovieDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java, "movie.db"
        ).build()

    @Provides
    @Singleton
    fun provideMovieDao(movieDatabase: MovieDatabase): MovieDao = movieDatabase.moviesDao()

    @Provides
    @Singleton
    fun provideKeysDao(movieDatabase: MovieDatabase): RemoteKeysDao = movieDatabase.remoteKeysDao()
}