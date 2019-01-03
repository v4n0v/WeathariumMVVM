package com.weatharium.v4n0v.weathariummvvm.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.repositories.images.RepoImages
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class GlideImageLoader : IImageLoader {

    private val app by lazy { App.instance }
    val repository: RepoImages = RepoImages()

    @SuppressLint("CheckResult")
    override fun loadPicture(name: String, link: String, callback: (Bitmap?) -> Unit) {
        val file = repository.readFromCache(name.toLowerCase())
        if (file != null) {
            Timber.d("loading weatherInfoData image from phone memory\n${file.absolutePath}")

            Observable.fromCallable {
                Glide.get(app).clearDiskCache()
            }.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        Glide.with(app)
                                .load(file)
                                .listener(object : OnRequestComplete<Drawable>() {
                                    override fun onRequestDataReady(resource: Drawable?) {
                                        callback((resource as BitmapDrawable).bitmap)
                                    }
                                })
                                .preload()
                    }

        } else {
            downloadPhoto(name, link, callback)
        }
    }

    override fun downloadPhoto(name: String, link: String, callback: (Bitmap?) -> Unit) {
        Glide.with(app).asBitmap()
                .load(link)
                .listener(object : OnRequestComplete<Bitmap>() {
                    override fun onRequestDataReady(resource: Bitmap?) {
                        repository.writeToCache(resource!!, name.toLowerCase())
                        callback(resource)
                    }
                })
                .preload()
    }
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

