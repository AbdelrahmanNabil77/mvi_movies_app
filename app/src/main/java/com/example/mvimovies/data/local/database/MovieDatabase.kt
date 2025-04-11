package com.example.mvimovies.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mvimovies.data.local.dao.MovieDao
import com.example.mvimovies.data.local.entity.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        const val DATABASE_NAME = "movie_database"
    }
}