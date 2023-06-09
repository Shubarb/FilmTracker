package com.example.filmtracker.models

import com.google.gson.annotations.SerializedName

data class CastAndCrewList (
    @SerializedName("id") var id: Int? = null,
    @SerializedName("cast") var castList: List<CastAndCrew> = arrayListOf(),
    @SerializedName("crew") var crewList: List<CastAndCrew> = arrayListOf(),
)