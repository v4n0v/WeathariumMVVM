package com.weatharium.v4n0v.weathariummvvm.fragments

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.weatharium.v4n0v.weathariummvvm.R
import com.weatharium.v4n0v.weathariummvvm.activities.ScrollingActivity
import com.weatharium.v4n0v.weathariummvvm.activities.State
import com.weatharium.v4n0v.weathariummvvm.components.Animator
import com.weatharium.v4n0v.weathariummvvm.databinding.ActivitySplashBinding
import com.weatharium.v4n0v.weathariummvvm.viewModels.SplashViewModel
import kotlinx.android.synthetic.main.activity_scrolling.*

class FragmentSplash : BaseFragment<ScrollingActivity>() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_splash, container, false)
        binding.viewModel = activity.run {
            ViewModelProviders.of(this).get(SplashViewModel::class.java)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

}