package com.example.filmtracker.view.home.fragment.homefragment

import com.example.filmtracker.models.Movie

interface HomeListener {
    fun onUpdateFromMovie(movie: Movie, isFavorite:Boolean)
//    fun onUpdateTitleMovie(movieTitle: String,isTitle: Boolean)
}