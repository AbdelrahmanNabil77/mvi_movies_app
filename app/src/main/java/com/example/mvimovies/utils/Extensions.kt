package com.example.mvimovies.utils

import com.example.mvimovies.data.local.entity.MovieEntity
import com.example.mvimovies.data.remote.dto.MovieDto
import com.example.mvimovies.domain.model.Movie

object Extensions {
    // Conversion extensions
    fun MovieDto.toEntity(remotePage: Int): MovieEntity = MovieEntity(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        releaseDate = releaseDate,
        voteAverage = voteAverage,
        remotePage = remotePage
    )

    fun MovieEntity.toMovie(): Movie = Movie(
        id = id,
        title = title,
        overview = overview,
        posterPath = posterPath,
        isFavorite = isFavorite,
        releaseDate = releaseDate,
        voteAverage = voteAverage
    )
}