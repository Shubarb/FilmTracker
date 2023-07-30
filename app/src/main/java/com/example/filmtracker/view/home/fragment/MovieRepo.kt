package com.example.filmtracker.view.home.fragment

import com.example.filmtracker.models.MovieList
import com.example.filmtracker.network.ApiDataSource
import com.example.filmtracker.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MovieRepo @Inject constructor(private val apiDataSource: ApiDataSource) {

//    suspend fun getMoviePopular(page: Int) : Flow<Resource<MovieList>> {
//        return flow {
//            emit(apiDataSource.getAllMovie(page))
//        }.flowOn(
//            Dispatchers.IO
//        )
//    }

//    suspend fun search(key: String): Flow<Resource<MovieList>> {
//        return flow {
//            emit(apiDataSource.search(key))
//        }.flowOn(
//            Dispatchers.IO
//        )
//    }
}