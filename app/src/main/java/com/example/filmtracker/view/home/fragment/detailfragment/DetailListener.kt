package com.example.filmtracker.view.home.fragment.detailfragment

import com.example.filmtracker.models.Movie

interface DetailListener {
    fun onUpdateFromDetail(movie: Movie, isFavorite: Boolean)
    fun onAddReminder()
//    fun onUpdateTitleFromDetail(movieTitle: String)
}