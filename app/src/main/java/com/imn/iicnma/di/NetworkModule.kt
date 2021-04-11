package com.imn.iicnma.di

import com.imn.iicnma.BuildConfig
import com.imn.iicnma.data.remote.API_BASE_URL
import com.imn.iicnma.data.remote.MovieService
import com.imn.iicnma.data.remote.popular.PopularMoviesService
import com.imn.iicnma.data.remote.search.SearchService
import com.imn.iicnma.data.repository.movies.MovieRemoteDataSource
import com.imn.iicnma.data.repository.popular.PopularMoviesRemoteDataSource
import com.imn.iicnma.data.repository.search.SearchRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {

        val apiKeyInterceptor: (Interceptor.Chain) -> Response = { chain ->
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            val newUrl: HttpUrl = originalUrl.newBuilder()
                .addQueryParameter("api_key", BuildConfig.API_KEY)
                .build()

            val newRequest = originalRequest.newBuilder()
                .url(newUrl)
                .build()

            chain.proceed(newRequest)
        }

        return Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                    .addInterceptor(apiKeyInterceptor)
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieService(retrofit: Retrofit): MovieService =
        retrofit.create(MovieService::class.java)

    @Provides
    @Singleton
    fun providesMovieRemoteDataSource(
        retrofit: Retrofit,
    ): MovieRemoteDataSource = retrofit.create(MovieService::class.java)

    @Provides
    @Singleton
    fun providesPopularMoviesRemoteDataSource(
        retrofit: Retrofit,
    ): PopularMoviesRemoteDataSource = retrofit.create(PopularMoviesService::class.java)

    @Provides
    @Singleton
    fun providesSearchRemoteDataSource(
        retrofit: Retrofit,
    ): SearchRemoteDataSource = retrofit.create(SearchService::class.java)
}