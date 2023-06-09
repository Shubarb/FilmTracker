package com.example.filmtracker.network

import com.example.filmtracker.models.CastAndCrewList
import com.example.filmtracker.models.MovieList
import retrofit2.Call
import retrofit2.Response

class ApiDataSource(private val apiService: ApiService) {
    suspend fun getAllMovie(movieCategory: String,apiKey: String,pageNumber:String): Response<MovieList> {
        return apiService.getMovieList(movieCategory,apiKey,pageNumber)
    }

//    suspend fun getAllCastCrew(id:Int,apiKey:String): Call<CastAndCrewList>{
//        return apiService.getCastAndCrew(id,apiKey)
//    }
}