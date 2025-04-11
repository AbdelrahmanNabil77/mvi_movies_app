package com.example.mvimovies.di

import com.example.mvimovies.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApiKey(): String {
        return BuildConfig.TMDB_API_KEY
    }
}