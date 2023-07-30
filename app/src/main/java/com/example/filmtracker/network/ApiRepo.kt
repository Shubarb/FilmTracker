package com.example.filmtracker.network

import android.app.Application
import android.util.Log
import com.example.filmtracker.database.MovieDao
import com.example.filmtracker.database.MovieDatabase
import com.example.filmtracker.models.CastAndCrewList
import com.example.filmtracker.models.Movie
import com.example.filmtracker.models.MovieList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.Response

class ApiRepo(
    application: Application
    ){
    private val mApiSerVice: ApiService = GetRetrofit().getInstance().create(ApiService::class.java)
    private val apiDataSource: ApiDataSource =ApiDataSource(mApiSerVice)
    private val homeDao: MovieDao
    init {
        val homeData: MovieDatabase = MovieDatabase.getInstance(application)
        homeDao = homeData.movieDao()
    }

    suspend fun addMovie(listmovie: Movie) = homeDao.addItems(listmovie)
    suspend fun deleteMovie(listmovie: Movie) = homeDao.deleteItem(listmovie)

    suspend fun getAllMovie(
        movieCategory: String,
        apiKey: String,
        pageNumber: String
    ): Flow<Resource<MovieList>> {
        return flow {
            emit(safeApi {
                apiDataSource.getAllMovie(movieCategory, apiKey, pageNumber)
            })
        }.flowOn(
            Dispatchers.IO
        )
    }

//    suspend fun getAllCastCrew(id:Int,apiKey:String): Flow<Resource<CastAndCrewList>>{
//        return flow {
//            emit(safeApi{
//                apiDataSource.getAllCastCrew(id,apiKey)
//            })
//        }.flowOn(
//            Dispatchers.IO
//        )
//    }

    private suspend fun <T> safeApi(apiCall: suspend () -> Response<T>): Resource<T> {
        try {
            val response = apiCall()
            val code = response.code()
            if (response.isSuccessful) {
                if (response.body() != null) {
                    return Resource.success(response.body()!!)
                }
            } else {
                Log.e("log", "$code")
                return Resource.failed(msg = "$code")

            }
            return Resource.failed(msg = "$code")

        } catch (e: Exception) {
            Log.e("log","${e.message}")
            return Resource.failed(msg = "${e.message}")
        }
    }

}