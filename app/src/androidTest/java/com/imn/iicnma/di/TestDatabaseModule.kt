package com.imn.iicnma.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.util.Executors
import com.imn.iicnma.data.local.MovieDatabase
import com.imn.iicnma.data.repository.favorites.FavoritesLocalDataSource
import com.imn.iicnma.data.repository.movies.MoviesLocalDataSource
import com.imn.iicnma.data.repository.popular.PopularMoviesLocalDataSource
import com.imn.iicnma.data.repository.search.SearchLocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DatabaseModule::class]
)
object TestDatabaseModule {

    @Provides
    @Singleton
    fun provideMovieDatabase(
        @ApplicationContext context: Context,
    ): MovieDatabase =
        Room.inMemoryDatabaseBuilder(
            context.applicationContext,
            MovieDatabase::class.java,
        )
            .allowMainThreadQueries()
            .setTransactionExecutor(Executors.mainThreadExecutor())
            .setQueryExecutor(Executors.mainThreadExecutor())
            .build()

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

    @Provides
    @Singleton
    fun providesMovieDao(database: MovieDatabase) = database.moviesDao()

    @Provides
    @Singleton
    fun providesFavoritesDao(database: MovieDatabase) = database.favoritesDao()

    @Provides
    @Singleton
    fun providesPopularDao(database: MovieDatabase) = database.popularMoviesDao()

}