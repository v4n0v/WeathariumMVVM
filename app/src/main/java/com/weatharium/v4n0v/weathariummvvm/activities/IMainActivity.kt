package com.weatharium.v4n0v.weathariummvvm.activities

import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo

interface IMainActivity {
    fun loadWeatherFromCache():WeatherInfo

    fun switchFragment(state:State)


}