package com.weatharium.v4n0v.weathariummvvm.activities


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.IBinder
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.R
import com.weatharium.v4n0v.weathariummvvm.components.CITY
import com.weatharium.v4n0v.weathariummvvm.components.channels.WeatherBus

import com.weatharium.v4n0v.weathariummvvm.databinding.ActivitySplashBinding
import com.weatharium.v4n0v.weathariummvvm.services.WeatherService
import com.weatharium.v4n0v.weathariummvvm.viewModels.SplashViewModel

import timber.log.Timber


class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    internal var bind: Boolean = false
    private lateinit var service: IBinder

    lateinit var sConn: ServiceConnection
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("SplashActivity onCreate")

        sConn = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                Timber.d("Service connected")
                this@SplashActivity.service = service
                bind = true
            }

            override fun onServiceDisconnected(name: ComponentName) {
                bind = false
            }
        }

        val serviceIntent = Intent(baseContext, WeatherService::class.java)
        bindService(serviceIntent, sConn, Context.BIND_AUTO_CREATE)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        App.instance.getAppComponent().inject(binding.viewModel!!)

        binding.viewModel?.cityNameData?.observe(this,
                Observer<String> {
                    val intent = Intent(this, ScrollingActivity::class.java)
                    intent.putExtra(CITY, it)
                    startActivity(intent)
                    finish()
                })

        binding.viewModel?.errorData?.observe(this,
                Observer {
                    //Timber.e(it)
                    showInformDialog("Ошибка", it?.message.toString()){
                        finish()
                    }
                })

        binding.executePendingBindings()
        binding.viewModel?.loadCityName()
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(sConn)
    }

}