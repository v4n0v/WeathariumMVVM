package com.weatharium.v4n0v.weathariummvvm.repositories.images

import android.graphics.Bitmap
import java.io.File

interface IImageCacheRepo {
      fun writeToCache(bitmap: Bitmap, city: String)
      fun readFromCache(city: String): File?
}