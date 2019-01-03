package com.weatharium.v4n0v.weathariummvvm.model.images

import com.google.gson.annotations.SerializedName


data class Photos(
        @SerializedName("photos")
        val photos: PhotosList,
        val status: String)

data class PhotosList(
        @SerializedName("perpage")
        val count: Int,
        @SerializedName("photo")
        val photo: List<Photo>)

data class Photo(val id: String,
                 @SerializedName("url_m")
                 val urlM: String)

