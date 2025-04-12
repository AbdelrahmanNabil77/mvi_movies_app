package com.example.mvimovies.data.local.entity

import android.util.Log
import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromGenreIds(genreIds: ArrayList<Int>?): String? {
        Log.d("hitler", "fromGenreIds: $genreIds")
        return genreIds?.joinToString(",")
    }

    @TypeConverter
    fun toGenreIds(data: String?): ArrayList<Int>? {
        Log.d("hitler", "toGenreIds: $data")
        return data?.split(",")
            ?.mapNotNull { it.toIntOrNull() }
            ?.let { ArrayList(it) }
    }
}