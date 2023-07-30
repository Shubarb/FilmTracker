package com.example.filmtracker.view.home.fragment.favoritefragment

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmtracker.database.FavoriteDao
import com.example.filmtracker.database.FavoriteDatabase
import com.example.filmtracker.models.Movie
import com.example.filmtracker.network.MovieRepo
import kotlinx.coroutines.launch

class MovieViewModel(app: Application) : ViewModel() {
    private val noteRepository: MovieRepo = MovieRepo(app)
    private val favoDao: FavoriteDao
    val countLiveData: LiveData<Int>
    init {
        val db = FavoriteDatabase.getInstance(app)
        favoDao = db.getNoteDao()
        countLiveData = favoDao.getCount()
    }

    var movieState = MutableLiveData<List<Movie>>()
    fun insertNote(note: Movie) {
        viewModelScope.launch {
            noteRepository.addMovieFavourite(note)
            movieState.value = noteRepository.getAllNote()
        }
    }

    fun deleteNote(note: Movie) = viewModelScope.launch {
        noteRepository.deleteMovieFavourite(note)
    }

    fun getAllNote() {
        viewModelScope.launch {
            movieState.value = noteRepository.getAllNote()
        }

    }

    fun getFavo(){
        viewModelScope.launch {
            movieState.value = noteRepository.getFavorite()
        }
    }
}