package com.example.filmtracker.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "table_movie_1")
data class Movie (
    @PrimaryKey
    @SerializedName("id") var id: Int?,
    @SerializedName("title") var title: String?,
    @SerializedName("overview") var overview: String?,
    @SerializedName("vote_average") var voteAverage: Double?,
    @SerializedName("release_date") var releaseDate: String?,
    @SerializedName("poster_path") var posterPath: String?,
    @SerializedName("adult") var adult: Boolean?,
    var isFavorite: Boolean = false,
    var reminderTime: String?,
    var reminderTimeDisplay: String?,
    var isload: Boolean? = false

): Serializable