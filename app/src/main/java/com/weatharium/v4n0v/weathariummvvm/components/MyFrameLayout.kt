package com.weatharium.v4n0v.weathariummvvm.components

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.widget.FrameLayout

class MyFrameLayout(context: Context) : FrameLayout(context)  {

     fun getXFraction():Float{
         return x/width
    }



      fun setXFraction( xFraction :Float) {
        // TODO: cache width

          x = if ((width > 0))  (xFraction * width) else -9999f
    }



}