package com.example.mvimovies.presentation.favorite

import com.example.mvimovies.domain.model.Movie
import com.example.mvimovies.presentation.base.BaseState

sealed class FavoriteState : BaseState {
    object Loading : FavoriteState()
    data class Success(val movies: List<Movie>) : FavoriteState()
    data class Error(val message: String) : FavoriteState()
    object Empty : FavoriteState()
}