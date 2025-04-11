package com.example.mvimovies.presentation.home

import androidx.paging.PagingData
import com.example.mvimovies.domain.model.Movie
import com.example.mvimovies.presentation.base.BaseState
import kotlinx.coroutines.flow.Flow

sealed class HomeState : BaseState {
    data object Idle : HomeState()
    data object Loading : HomeState()
    data class Success(
        val movies: Flow<PagingData<Movie>>,
        val scrollPosition: Int = 0
    ) : HomeState()
    data class Error(val message: String) : HomeState()
}