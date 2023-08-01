package com.example.filmtracker.view.home.fragment.homefragment

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmtracker.network.ApiDataSource

class HomeViewModelFactory(private val app: Application):
    ViewModelProvider.Factory {
    private val apiData: ApiDataSource? = null
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(app) as T
        }
        throw IllegalArgumentException("Unable construct viewmodel")
    }
}