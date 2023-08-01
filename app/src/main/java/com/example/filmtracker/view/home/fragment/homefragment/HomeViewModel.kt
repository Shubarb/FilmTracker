package com.example.filmtracker.view.home.fragment.homefragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.filmtracker.models.MovieList
import com.example.filmtracker.network.ApiRepo
import com.example.filmtracker.network.Resource
import kotlinx.coroutines.launch

class HomeViewModel(app: Application) : ViewModel() {
    private val apiRepo: ApiRepo = ApiRepo(app)

    private val _stateListMovie = MutableLiveData<Resource<MovieList>>()
    val stateListMovie: LiveData<Resource<MovieList>> = _stateListMovie

//    private val _listState = MutableLiveData<List<Movie>>()
//    val listState: LiveData<List<Movie>> = _listState

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


}