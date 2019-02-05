package com.weatharium.v4n0v.weathariummvvm.viewModels

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import com.weatharium.v4n0v.weathariummvvm.repositories.weather.IWeatherRepo
import javax.inject.Inject

@SuppressLint("CheckResult")
class HistoryViewModel:ViewModel(){

    val historyWeatherData   : MutableLiveData<ArrayList<WeatherInfo>> by lazy { MutableLiveData<ArrayList<WeatherInfo>>() }
    @Inject
    lateinit var repoWeather: IWeatherRepo
    fun getWeatherHistory(){
        repoWeather.loadWeatherHistory().subscribe { map->
            val list = map.values.sortedByDescending { it.time }
            historyWeatherData.value = ArrayList(list)
        }
    }

}