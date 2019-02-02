package com.weatharium.v4n0v.weathariummvvm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class WeatherInfo(
        @SerializedName("coord")
        @Expose
        val coord: Coord,
        @SerializedName("weather")
        @Expose
        var weather: List<Weather>?,
        @SerializedName("main")
        @Expose
        val main: Main,
        val wind: Wind,
        @SerializedName("clouds")
        @Expose
        val clouds: Clouds,
        val dt: Int,
        @SerializedName("sys")
        @Expose
        val sys: Sys,
        val id: Int,
        val name: String,
        val cod: Int,

        var time: Date?
)