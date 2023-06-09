package com.example.filmtracker.view.home.fragment.homefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.filmtracker.database.MovieDatabase
import com.example.filmtracker.network.ApiRepo

class HomeViewModelFactory(private val apiRepo: ApiRepo, private val db: MovieDatabase):
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(apiRepo, db) as T
    }
}