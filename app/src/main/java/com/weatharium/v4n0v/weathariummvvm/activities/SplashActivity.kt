package com.weatharium.v4n0v.weathariummvvm.activities


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.R
import com.weatharium.v4n0v.weathariummvvm.components.CITY

import com.weatharium.v4n0v.weathariummvvm.databinding.ActivitySplashBinding
import com.weatharium.v4n0v.weathariummvvm.viewModels.SplashViewModel

import timber.log.Timber


class SplashActivity : BaseActivity() {
    override fun switchFragment(state: State) {

    }

    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("SplashActivity onCreate")
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.viewModel= ViewModelProviders.of(this).get(SplashViewModel::class.java)
        App.instance.getAppComponent().inject(binding.viewModel!!)
        binding.viewModel?.isSplashCompleteData?.observe(this,
                Observer<Boolean> {
                    val intent = Intent(this, ScrollingActivity::class.java)
                    intent.putExtra(CITY, binding.viewModel?.city)
                    startActivity(intent)
                    finish()
                })
        binding.executePendingBindings()
        binding.viewModel?.loadCityName()
    }


}