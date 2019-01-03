package com.weatharium.v4n0v.weathariummvvm.fragments

import android.content.Context
import android.support.v4.app.Fragment
import com.weatharium.v4n0v.weathariummvvm.activities.BaseActivity

abstract class BaseFragment<T : BaseActivity> : Fragment(){
    lateinit var activity: T

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        activity = context as T
    }
}