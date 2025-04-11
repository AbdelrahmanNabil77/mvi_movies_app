package com.example.mvimovies.domain.usecase

import androidx.paging.PagingData
import com.example.mvimovies.domain.model.Movie
import com.example.mvimovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    operator fun invoke(): Flow<List<Movie>> = repository.getFavoriteMovies()
}