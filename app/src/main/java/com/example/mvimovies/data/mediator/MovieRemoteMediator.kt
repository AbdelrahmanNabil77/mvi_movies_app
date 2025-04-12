package com.example.mvimovies.data.mediator

import android.annotation.SuppressLint
import android.net.http.HttpException
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.mvimovies.BuildConfig
import com.example.mvimovies.data.local.dao.MovieDao
import com.example.mvimovies.data.local.database.MovieDatabase
import com.example.mvimovies.data.local.entity.MovieEntity
import com.example.mvimovies.data.remote.api.MovieApiService
import com.example.mvimovies.utils.Extensions.toEntity
import javax.inject.Inject

@ExperimentalPagingApi
@OptIn(ExperimentalPagingApi::class)
class MovieRemoteMediator @Inject constructor(
    private val api: MovieApiService,
    private val database: MovieDatabase,
    private val apiKey: String
) : RemoteMediator<Int, MovieEntity>() {

    private val dao get() = database.movieDao()

    @SuppressLint("NewApi")
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        return try {
            // Determine page number based on load type
            val page = when (loadType) {
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    lastItem?.remotePage?.plus(1) ?: 1
                }
            }

            // Fetch movies from API
            val response = api.discoverMovies(
                page = page,
                apiKey = apiKey
            )

            database.withTransaction {
                    // Merge with existing favorites
                    val existingMovies = dao.getMoviesRaw()
                if (response.isSuccessful){
                    response.body()?.let {
                        val mergedMovies = it.results.map { dto ->
                            val movie = existingMovies.find{it.id == 324544}
                            existingMovies.find { it.id == dto.id }?.copy(
                                title = dto.title,
                                overview = dto.overview,
                                posterPath = dto.posterPath,
                                remotePage = page,
                                genreIds = dto.genreIds
                            ) ?: dto.toEntity(remotePage = page)
                        }
                        dao.insertMovies(mergedMovies)

                    }
                }else if(!response.isSuccessful && existingMovies.isEmpty()){
                    throw Exception(response.errorBody()?.string())
                } else {
                    dao.insertMovies(existingMovies)
                }

                // Insert new movies with page number
//                dao.insertMovies(
//                    response.results.map {
//                        it.toEntity(remotePage = page)
//                    }
//                )
            }
            
            // Calculate end of pagination
            val endOfPaginationReached = page >= response?.body()?.totalPages?:0

            MediatorResult.Success(
                endOfPaginationReached = endOfPaginationReached
            )
        } catch (e: Exception) {
            Log.d("hitler", "error mediator ${e.message}")
            MediatorResult.Error(e)
        }
    }
}