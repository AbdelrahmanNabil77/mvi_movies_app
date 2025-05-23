package com.example.mvimovies.di

import com.example.mvimovies.BuildConfig
import com.example.mvimovies.data.local.dao.MovieDao
import com.example.mvimovies.data.local.database.MovieDatabase
import com.example.mvimovies.data.remote.api.MovieApiService
import com.example.mvimovies.data.repository.MovieRepositoryImpl
import com.example.mvimovies.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMovieRepository(
        api: MovieApiService,
        movieDatabase: MovieDatabase,
        dao: MovieDao
    ): MovieRepository {
        return MovieRepositoryImpl(api = api,
            dao = dao,
            movieDatabase = movieDatabase,
            apiKey = BuildConfig.TMDB_API_KEY)
    }
}