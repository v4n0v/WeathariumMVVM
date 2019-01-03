package com.weatharium.v4n0v.weathariummvvm.api

import android.widget.ImageView
import com.weatharium.v4n0v.weathariummvvm.model.images.Photos
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

@BaseUrl("https://api.flickr.com/services/rest/")
interface ImageApi {
    @GET("?safe_search=safe&format=json&content_type=1&sort=relevance&method=flickr.photos.search&media=photos&nojsoncallback=1")
    fun getImage(
            @Query("api_key") key: String,
            @Query("extras") extras: String,
            @Query("per_page") qty: Int,
            @Query("text") text: String
    ): Observable<Photos>
}