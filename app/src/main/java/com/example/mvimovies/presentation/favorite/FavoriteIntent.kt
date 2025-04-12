package com.example.mvimovies.presentation.favorite

import com.example.mvimovies.presentation.base.BaseIntent

sealed class FavoriteIntent : BaseIntent {
    object LoadFavorites : FavoriteIntent()
    data class RemoveFavorite(val movieId: Int) : FavoriteIntent()
}