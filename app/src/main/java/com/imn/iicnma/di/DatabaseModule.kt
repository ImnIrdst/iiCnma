package com.imn.iicnma.di

import android.content.Context
import androidx.room.Room
import com.imn.iicnma.data.local.MovieDatabase
import com.imn.iicnma.data.repository.favorites.FavoritesLocalDataSource
import com.imn.iicnma.data.repository.movies.MoviesLocalDataSource
import com.imn.iicnma.data.repository.popular.PopularMoviesLocalDataSource
import com.imn.iicnma.data.repository.search.SearchLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
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
    fun providesPopularMoviesDataSource(
        database: MovieDatabase,
    ): PopularMoviesLocalDataSource = database.popularMoviesDao()

    @Provides
    @Singleton
    fun providesSearchDataSource(
        database: MovieDatabase,
    ): SearchLocalDataSource = database.searchDao()

    @Provides
    @Singleton
    fun providesFavoritesDataSource(
        database: MovieDatabase,
    ): FavoritesLocalDataSource = database.favoritesDao()

    @Provides
    @Singleton
    fun providesMovieDataSource(
        database: MovieDatabase,
    ): MoviesLocalDataSource = database.moviesDao()
}