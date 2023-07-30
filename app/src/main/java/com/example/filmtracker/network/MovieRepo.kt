package com.example.filmtracker.network

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.filmtracker.database.FavoriteDao
import com.example.filmtracker.database.FavoriteDatabase
import com.example.filmtracker.models.Movie

class MovieRepo(application: Application) {
    private val noteDao: FavoriteDao
    init {
        val noteDataBase: FavoriteDatabase = FavoriteDatabase.getInstance(application)
        noteDao = noteDataBase.getNoteDao()
    }

    suspend fun addMovieFavourite(movie: Movie) = noteDao.addMovieFavourite(movie)
    suspend fun deleteMovieFavourite(movie: Movie) = noteDao.deleteMovieFavourite(movie)

    suspend fun getAllNote(): List<Movie> = noteDao.getAllFavorite()
    suspend fun getFavorite(): List<Movie> = noteDao.getTickedMovies()

}