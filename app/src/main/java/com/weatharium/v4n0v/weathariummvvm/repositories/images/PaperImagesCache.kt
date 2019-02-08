package com.weatharium.v4n0v.weathariummvvm.repositories.images

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Environment
import com.bumptech.glide.Glide
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.api.ApiFactory
import com.weatharium.v4n0v.weathariummvvm.components.*
import com.weatharium.v4n0v.weathariummvvm.model.images.Photos
import io.paperdb.Paper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

@SuppressLint("CheckResult")
class PaperImagesCache(private val apiFactory: ApiFactory, private val app: App) : IImageCacheRepo {

    override fun getPhotosFromFlickr(city: String): Observable<Photos?> {
        return apiFactory.getImageApi().getImage("$city+$FLIKR_REQUEST_IMAGE_KEY")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

  //  /storage/emulated/0/Android/data/com.weatharium.v4n0v.weathariummvvm/files/Pictures/63b04a37-1849-394e-b386-4687adcb410a.jpg
    override fun writeToCache(bitmap: Bitmap, city: String) {
        var imageFile = File(App.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${city.toUUID()}.jpg")

        if (imageFile.exists()) {
            imageFile.delete()
            imageFile = File(App.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${city.toUUID()}.jpg")
        }

        FileOutputStream(imageFile).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it)
            it.flush()
        }

        Paper.book(BOOK_IMAGES).delete(city)
        Paper.book(BOOK_IMAGES).write(city.toLowerCase(), imageFile)
    }

    override fun readFromCache(city: String): Observable<File?> {
        return Observable.create { e ->
            val file = Paper.book(BOOK_IMAGES).read<File?>(city.toLowerCase())
            if (file != null)
                e.onNext(file)

            e.onComplete()
        }

    }


    override fun loadPictureFromPath(path: File, callback: (Bitmap?) -> Unit) {
        Glide.get(app).clearMemory()
        Observable.fromCallable {
            Glide.get(app).clearDiskCache()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Glide.with(app)
                            .load(path)
                            .listener(object : OnRequestComplete<Drawable>() {
                                override fun onRequestDataReady(resource: Drawable?) {
                                    callback((resource as BitmapDrawable).bitmap)
                                }
                            })
                            .preload()
                }
    }


    override fun loadPicture(name: String, callback: (Bitmap?) -> Unit) {
        var file: File? = null
        readFromCache(name.toLowerCase()).doOnComplete {
            if (file != null) {
                Timber.d("loading weatherInfoData image from phone memory\n${file?.absolutePath}")
                loadPictureFromPath(file!!, callback)
            } else {
                getPhotosFromFlickr(name).subscribe { photos ->
                    photos?.let {
                        val index = random(0, it.photos.photo.size - 1)
                        downloadPhoto(name, it.photos.photo[index].urlM, callback)
                    }
                }
            }
        }.subscribe { f ->
            file = f
        }
    }

    override fun downloadPhoto(name: String, link: String, callback: (Bitmap?) -> Unit) {
        Glide.with(app).asBitmap()
                .load(link)
                .listener(object : OnRequestComplete<Bitmap>() {
                    override fun onRequestDataReady(resource: Bitmap?) {
                        writeToCache(resource!!, name.toLowerCase())
                        callback(resource)
                    }
                })
                .preload()
    }

}