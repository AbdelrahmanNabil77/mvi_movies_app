package com.example.mvimovies.domain.usecase

import com.example.mvimovies.domain.repository.MovieRepository
import javax.inject.Inject

class UpdateFavoriteUseCase @Inject constructor(
    private val repository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int, isFavorite: Boolean) {
        repository.updateFavorite(movieId, isFavorite)
    }
}