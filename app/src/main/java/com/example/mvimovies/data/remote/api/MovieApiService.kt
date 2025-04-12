package com.example.mvimovies.data.remote.api

import com.example.mvimovies.data.remote.dto.MoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String,
    ): Response<MoviesResponse>
}