package com.weatharium.v4n0v.weathariummvvm.repositories.images

import android.graphics.Bitmap
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.weatharium.v4n0v.weathariummvvm.model.images.Photos
import io.reactivex.Observable
import java.io.File

interface IImageCacheRepo {
      fun writeToCache(bitmap: Bitmap, city: String)
      fun readFromCache(city: String): Observable<File?>
      fun getPhotosFromFlickr(city: String):  Observable<Photos?>

      fun loadPicture(name: String, callback: (Bitmap?) -> Unit)
      fun downloadPhoto(name: String, link: String) :Observable<Bitmap>
      fun loadPictureFromPath(path: File, callback: (Bitmap?) -> Unit)
}

abstract class OnRequestComplete<T> : RequestListener<T> {
    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<T>?, isFirstResource: Boolean): Boolean {
        return false
    }

    override fun onResourceReady(resource: T?, model: Any?, target: Target<T>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
        onRequestDataReady(resource)
        return false
    }

    abstract fun onRequestDataReady(resource: T?)
}