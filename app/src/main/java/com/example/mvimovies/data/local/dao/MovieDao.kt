package com.example.mvimovies.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.mvimovies.data.local.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies ORDER BY voteAverage DESC")
    fun getMovies(): PagingSource<Int, MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovies(movies: List<MovieEntity>)

    @Query("UPDATE movies SET isFavorite = :isFavorite WHERE id = :movieId")
    fun updateFavorite(movieId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM movies WHERE isFavorite = 1 ORDER BY voteAverage DESC")
    fun getFavoriteMovies(): Flow<List<MovieEntity>>
}