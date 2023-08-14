package com.example.filmtracker.network

import android.app.Application
import com.example.filmtracker.database.FavoriteDao
import com.example.filmtracker.database.RoomDatabases
import com.example.filmtracker.database.RemindDao
import com.example.filmtracker.models.Movie

class MovieRepo(application: Application) {
    private val favoriteDao: FavoriteDao
    private val remindDao: RemindDao
    init {
        val noteDataBase: RoomDatabases = RoomDatabases.getInstance(application)
        favoriteDao = noteDataBase.getNoteDao()
        remindDao = noteDataBase.getRemindDao()
    }

    suspend fun addMovieFavourite(movie: Movie) = favoriteDao.addMovieFavourite(movie)
    suspend fun deleteMovieFavourite(movie: Movie) = favoriteDao.deleteMovieFavourite(movie)
    suspend fun getAllNote(): List<Movie> = favoriteDao.getAllFavorite()

    suspend fun addMovieRemind(movie: Movie) = remindDao.addMovieRemind(movie)
    suspend fun removeMovieRemind(movie: Movie) = remindDao.deleteMovieRemind(movie)
    suspend fun getAllRemind() :List<Movie> = remindDao.getAllRemind()

}