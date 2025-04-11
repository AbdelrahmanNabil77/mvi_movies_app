package com.example.mvimovies.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.mvimovies.data.local.dao.MovieDao
import com.example.mvimovies.data.local.entity.MovieEntity
import com.example.mvimovies.data.remote.api.MovieApiService
import com.example.mvimovies.utils.Extensions.toEntity
import javax.inject.Inject

@ExperimentalPagingApi
class MovieRemoteMediator @Inject constructor(
    private val api: MovieApiService,
    private val dao: MovieDao,
    private val apiKey: String
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.remotePage?.plus(1) ?: 1
                }
            }

            val response = api.discoverMovies(
                page = page,
                apiKey = apiKey,
            )

            dao.insertMovies(
                response.results.map {
                    it.toEntity(remotePage = page)
                }
            )

            MediatorResult.Success(
                endOfPaginationReached = response.results.isEmpty()
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}