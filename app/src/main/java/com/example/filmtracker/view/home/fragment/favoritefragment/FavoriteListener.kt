package com.example.filmtracker.view.home.fragment.favoritefragment

import com.example.filmtracker.models.Movie

interface FavoriteListener {
    fun onUpdateFromFavorite(movie: Movie)
}