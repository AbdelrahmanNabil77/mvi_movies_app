package com.example.mvimovies.presentation.favorite

import androidx.lifecycle.viewModelScope
import com.example.mvimovies.domain.repository.MovieRepository
import com.example.mvimovies.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val repository: MovieRepository
) : BaseViewModel<FavoriteState, FavoriteIntent>() {

    override fun processIntent(intent: FavoriteIntent) {
        when (intent) {
            FavoriteIntent.LoadFavorites -> loadFavorites()
            is FavoriteIntent.RemoveFavorite -> removeFavorite(intent.movieId)
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            setState(FavoriteState.Loading)
            try {
                repository.getFavoriteMovies().collect { movies ->
                    if (movies.isEmpty()) {
                        setState(FavoriteState.Empty)
                    } else {
                        setState(FavoriteState.Success(movies))
                    }
                }
            } catch (e: Exception) {
                setState(FavoriteState.Error(e.message ?: "Unknown error"))
            }
        }
    }

    private fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            try {
                repository.updateFavorite(movieId, false)
                loadFavorites() // Refresh the list after removal
            } catch (e: Exception) {
                setState(FavoriteState.Error("Failed to remove favorite"))
            }
        }
    }
}