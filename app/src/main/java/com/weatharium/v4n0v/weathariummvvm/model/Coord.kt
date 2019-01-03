package com.weatharium.v4n0v.weathariummvvm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class Coord(
        @SerializedName("lon")
        @Expose
        private var lon: Double,

        @SerializedName("lat")
        @Expose
        private var lat: Double
)