package com.weatharium.v4n0v.weathariummvvm.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Sys(
        @SerializedName("message")
        @Expose
        var message: Double,
        @SerializedName("country")
        @Expose
        var country: String,
        @SerializedName("sunrise")
        @Expose
        var sunrise: Int,
        @SerializedName("sunset")
        @Expose
        var sunset: Int
)

