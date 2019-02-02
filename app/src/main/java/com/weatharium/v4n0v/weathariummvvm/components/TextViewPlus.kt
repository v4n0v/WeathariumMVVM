package com.weatharium.v4n0v.weathariummvvm.components

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.weatharium.v4n0v.weathariummvvm.R
import timber.log.Timber

class TextViewPlus : android.support.v7.widget.AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setCustomFont(context, attrs)
    }
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setCustomFont(context, attrs)
    }

    private fun setCustomFont(ctx: Context, attrs: AttributeSet) {
        val a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus)
        val customFont = a.getString(R.styleable.TextViewPlus_customFont)
        setCustomFont(ctx, customFont)
        a.recycle()
    }

    private fun setCustomFont(ctx: Context, asset: String?): Boolean {
        val tf: Typeface
        try {
            tf = Typeface.createFromAsset(ctx.assets, asset)
        } catch (e: Exception) {
            Timber.e("Could not get typeface: ${ e.message}")
            return false
        }
        typeface = tf
        return true
    }

}
