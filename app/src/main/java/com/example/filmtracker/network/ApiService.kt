package com.example.filmtracker.network

import com.example.filmtracker.models.CastAndCrewList
import com.example.filmtracker.models.MovieList
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("3/movie/{movieCategory}")
    suspend fun getMovieList(@Path("movieCategory") movieCategory: String, @Query("api_key") apiKey: String, @Query("page") pageNumber:String): Response<MovieList>

//    @GET("3/movie/{movieId}/credits")
//    suspend fun getCastAndCrew(@Path("movieId") id:Int, @Query("api_key") apiKey:String): Call<CastAndCrewList>
}