package com.example.filmtracker.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.filmtracker.models.DetailMovie
import com.example.filmtracker.models.Movie

@Dao
interface FavoriteDao {
    @Insert
    suspend fun addMovieFavourite(movie: Movie)

    @Delete
    suspend fun deleteMovieFavourite(movie: Movie)

    @Query("SELECT * FROM table_movie_1")
    suspend fun getAllFavorite(): List<Movie>

    @Query("SELECT COUNT(*) FROM table_movie_1")
    fun getCount(): LiveData<Int>

    @Query("SELECT * FROM table_movie_1 WHERE isFavorite = 1")
    suspend fun getTickedMovies(): List<Movie>
}