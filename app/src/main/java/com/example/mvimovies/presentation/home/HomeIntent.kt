package com.example.mvimovies.presentation.home

import com.example.mvimovies.presentation.base.BaseIntent

sealed class HomeIntent : BaseIntent {
    data object LoadMovies : HomeIntent()
    data class UpdateScrollPosition(val position: Int) : HomeIntent()
    data class UpdateFavorite(val movieId: Int, val isFavorite: Boolean) : HomeIntent()
}