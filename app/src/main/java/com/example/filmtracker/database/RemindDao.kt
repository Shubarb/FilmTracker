package com.example.filmtracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.filmtracker.models.Movie

@Dao
interface RemindDao {
    @Insert
    suspend fun addMovieRemind(movie: Movie)

    @Delete
    suspend fun deleteMovieRemind(movie: Movie)

    @Query("SELECT * FROM table_movie_1")
    suspend fun getAllRemind(): List<Movie>

    @Query("SELECT COUNT(*) FROM table_movie_1")
    fun getCount(): LiveData<Int>
}