package com.example.mvimovies.domain.repository


import androidx.paging.PagingData
import com.example.mvimovies.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(): Flow<PagingData<Movie>>
    suspend fun updateFavorite(movieId: Int, isFavorite: Boolean)
    fun getFavoriteMovies(): Flow<List<Movie>>
}