package com.example.filmtracker.view.home.fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmtracker.database.FavoriteDao
import com.example.filmtracker.database.FavoriteDatabase
import com.example.filmtracker.models.CastAndCrewList
import com.example.filmtracker.models.Movie
import com.example.filmtracker.models.MovieList
import com.example.filmtracker.network.ApiRepo
import com.example.filmtracker.network.MovieRepo
import com.example.filmtracker.network.Resource
import kotlinx.coroutines.launch

class MovieViewModel(app: Application) : ViewModel() {
    private val noteRepository: MovieRepo = MovieRepo(app)
    private val favoDao: FavoriteDao
    private val apiRepo: ApiRepo = ApiRepo(app)

    private val _stateListMovie = MutableLiveData<Resource<MovieList>>()
    val stateListMovie: LiveData<Resource<MovieList>> = _stateListMovie

    private val _stateListDetail = MutableLiveData<Resource<CastAndCrewList>>()
    val stateListDetail: LiveData<Resource<CastAndCrewList>> = _stateListDetail

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


    fun getAllMovie(movieCategory: String, apiKey: String, pageNumber: String) {
        viewModelScope.launch {
            _stateListMovie.value = Resource.loading()
            apiRepo.getAllMovie(movieCategory, apiKey, pageNumber).collect() {
                if (it.status == Resource.Status.SUCCESS) {
                    if (it.data != null) {
                        Log.e("log", "Call Data Success Data Khác Null")
                        _stateListMovie.value = Resource.success(it.data)
//                        addItem(it.data.result)
                    } else {
                        Log.e("log", "Call Data Success Data = Null")
                        _stateListMovie.value = Resource.failed(msg = "${it.message}")
                    }
                } else {
                    Log.e("log", "Call Data Fail")
                    _stateListMovie.value = Resource.failed(msg = "${it.message}")
                }
            }
        }
    }

    fun getCastAndCrew(id:Int, apiKey:String) {
        viewModelScope.launch {
            _stateListDetail.value = Resource.loading()
            apiRepo.getAllCastCrew(id, apiKey).collect() {
                if (it.status == Resource.Status.SUCCESS) {
                    if (it.data != null) {
                        Log.e("log", "Call Data Success Data Khác Null")
                        _stateListDetail.value = Resource.success(it.data)
//                        addItem(it.data.result)
                    } else {
                        Log.e("log", "Call Data Success Data = Null")
                        _stateListDetail.value = Resource.failed(msg = "${it.message}")
                    }
                } else {
                    Log.e("log", "Call Data Fail")
                    _stateListDetail.value = Resource.failed(msg = "${it.message}")
                }
            }
        }
    }

}