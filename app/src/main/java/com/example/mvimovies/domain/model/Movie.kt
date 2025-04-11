package com.example.mvimovies.domain.model

data class Movie(
    val id: Int?,
    val title: String?,
    val overview: String?,
    val posterPath: String?,
    val isFavorite: Boolean?,
    val releaseDate: String?,
    val voteAverage: Double?
)