package com.weatharium.v4n0v.weathariummvvm.components

import android.graphics.Bitmap

interface IImageLoader {
    fun loadPicture(name: String, link: String, callback: (Bitmap?) -> Unit)
    fun downloadPhoto(name: String, link: String, callback: (Bitmap?) -> Unit)
}