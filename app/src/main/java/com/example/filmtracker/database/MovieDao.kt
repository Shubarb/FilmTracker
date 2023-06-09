package com.example.filmtracker.database

import androidx.room.*
import com.example.filmtracker.models.Movie

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addItems(movie: List<Movie>)

    @Delete
    suspend fun deleteItem(movie: Movie)

    @Update
    suspend fun updateItem(movie: Movie)

    @Query("SELECT * FROM table_movie_1")
    suspend fun getAllData(): List<Movie>

}