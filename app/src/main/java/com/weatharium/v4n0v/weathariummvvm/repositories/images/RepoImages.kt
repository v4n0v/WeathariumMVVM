package com.weatharium.v4n0v.weathariummvvm.repositories.images

import android.graphics.Bitmap
import android.os.Environment
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.components.BOOK_IMAGES
import com.weatharium.v4n0v.weathariummvvm.components.toMD5
import io.paperdb.Paper
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream

class RepoImages : IImageCacheRepo {
    override fun writeToCache(bitmap: Bitmap, city: String) {
        var imageFile = File(App.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "${city.toMD5()}.jpg")
//        var imageFile = File(App.instance.filesDir, "$city.jpg")
        val isDelete:Boolean
        if (imageFile.exists()) {
            isDelete = imageFile.delete()
            imageFile = File(App.instance.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "$city.jpg")
//            imageFile = File(App.instance.filesDir, "$city.jpg")
        }
        if (imageFile.exists())
            Timber.d("WTF!?!")

        FileOutputStream(imageFile).use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, it)
            it.flush()
        }

       val s  = Paper.book(BOOK_IMAGES).read<File>(city)

        Paper.book(BOOK_IMAGES).delete(city)
        val s1  = Paper.book(BOOK_IMAGES).read<File>(city)
        Paper.book(BOOK_IMAGES).write(city, imageFile)
    }

    override fun readFromCache(city: String): File? {
        return Paper.book(BOOK_IMAGES).read(city)
    }


}