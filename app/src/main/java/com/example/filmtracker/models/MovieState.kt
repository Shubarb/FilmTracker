package com.example.filmtracker.models

class MovieState(
    val status: Status,
    val data: Any?,
    val message: String?
) {
    enum class Status{
        LOADING, SUCCESS , FAILED,
        SUCCESS_SEARCH,
        LOADING_SEARCH
    }

    companion object{
        fun loading() = MovieState(Status.LOADING, data = null, message = null)
        fun loadingSearch() = MovieState(Status.LOADING_SEARCH, data = null, message = null)
        fun successGetMovie(data: Any) = MovieState(Status.SUCCESS, data, message = "")
        fun successSearch(data: Any) = MovieState(Status.SUCCESS_SEARCH, data, message = "")
        fun error(msg: String) = MovieState(Status.FAILED, data = null, msg)
    }
}