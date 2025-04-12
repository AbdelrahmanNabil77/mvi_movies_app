package com.example.mvimovies.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieEntity(
    @PrimaryKey val id: Int?,
    val title: String?,
    val overview: String?,
    val posterPath: String?,
    val isFavorite: Boolean = false,
    val releaseDate: String?,
    val voteAverage: Double?,
    val remotePage: Int? = null,
    var genreIds: ArrayList<Int>? = null,
)