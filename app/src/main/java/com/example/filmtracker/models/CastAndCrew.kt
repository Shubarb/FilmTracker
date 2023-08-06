package com.example.filmtracker.models

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

@Entity(tableName = "table_character_1")
data class CastAndCrew (
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("profile_path")
    var profilePath: String? = null
)