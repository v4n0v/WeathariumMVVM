package com.weatharium.v4n0v.weathariummvvm.repositories

import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import io.reactivex.Observable

interface IWeatherRepo {
    fun loadCity(): Observable<String>
    fun saveCity(city: String)
    fun loadWeather(city: String): Observable<WeatherInfo?>
    fun downLoadWeather(city: String): Observable<WeatherInfo>
    fun saveWeather(city: String, weatherInfo: WeatherInfo)
    fun loadWeatherHistory(): Observable<HashMap<String, WeatherInfo>>
   // fun loadLastWeather():Observable<WeatherInfo?>
}