package com.example.mvimovies.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.mvimovies.data.local.dao.MovieDao
import com.example.mvimovies.data.mediator.MovieRemoteMediator
import com.example.mvimovies.data.remote.api.MovieApiService
import com.example.mvimovies.domain.model.Movie
import com.example.mvimovies.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.paging.map
import com.example.mvimovies.data.local.database.MovieDatabase
import com.example.mvimovies.data.local.entity.MovieEntity
import com.example.mvimovies.utils.Extensions.toMovie
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryImpl @Inject constructor(
    private val api: MovieApiService,
    private val dao: MovieDao,
    private val movieDatabase: MovieDatabase,
    private val apiKey: String
) : MovieRepository {

    override fun getMovies(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance=5,
                enablePlaceholders = false
            ),
            remoteMediator = MovieRemoteMediator(api, movieDatabase, apiKey),
            pagingSourceFactory = { dao.getMovies() }
        ).flow.map { pagingData ->
            pagingData.map { it.toMovie() }
        }
    }

    override suspend fun updateFavorite(movieId: Int, isFavorite: Boolean) {
        dao.updateFavorite(movieId= movieId, isFavorite= isFavorite)
    }

    override fun getFavoriteMovies(): Flow<List<Movie>> {
        return dao.getFavoriteMovies().map { list -> list.map { it.toMovie() } }
    }
}