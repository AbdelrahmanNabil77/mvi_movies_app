package com.example.mvimovies.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.mvimovies.domain.usecase.GetMoviesUseCase
import com.example.mvimovies.domain.usecase.UpdateFavoriteUseCase
import com.example.mvimovies.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase
) : BaseViewModel<HomeState, HomeIntent>() {

    private var scrollPosition = 0

//    init {
//        processIntent(HomeIntent.LoadMovies)
//    }

    override fun processIntent(intent: HomeIntent) {
        when (intent) {
            HomeIntent.LoadMovies -> loadMovies()
            is HomeIntent.UpdateScrollPosition -> updateScrollPosition(intent.position)
            is HomeIntent.UpdateFavorite -> updateFavorite(intent.movieId, intent.isFavorite)
        }
    }

    private fun loadMovies() {
        setState(HomeState.Loading)
        try {
            val movies = getMoviesUseCase()
                .cachedIn(viewModelScope)

            setState(HomeState.Success(
                movies = movies
            ))
        } catch (e: Exception) {
            setState(HomeState.Error(e.message ?: "Unknown error"))
        }
    }

    private fun updateScrollPosition(position: Int) {
        scrollPosition = position

    }

    private fun updateFavorite(movieId: Int, isFavorite: Boolean) {
        viewModelScope.launch {
            updateFavoriteUseCase(movieId, isFavorite)
        }
    }
}