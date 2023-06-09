package com.example.filmtracker.network

class Resource <out T>(
    val status: Status,
    val data : T? = null,
    val message: String? = null
) {
    enum class Status {
        SUCCESS, FAILED, LOADING
    }

    companion object {
        fun <T> success(data: T) = Resource<T>(Status.SUCCESS, data, message = null)
        fun <T> loading(data: T? = null) = Resource<T>(Status.LOADING, data, message = null)
        fun <T> failed(data: T? = null, msg: String) =
            Resource<T>(Status.FAILED, data, message = msg)
    }
}