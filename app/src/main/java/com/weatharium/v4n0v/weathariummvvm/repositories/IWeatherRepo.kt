package com.weatharium.v4n0v.weathariummvvm.repositories

import android.graphics.Bitmap
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import io.reactivex.Observable
import io.reactivex.ObservableEmitter

interface IWeatherRepo {
    fun loadCity(): Observable<String>
    fun saveCity(city: String)
        fun loadWeather(city: String): Observable<WeatherInfo>
//    fun loadWeather(city: String, e: ObservableEmitter<WeatherInfo>)

    fun saveWeather(city: String, weatherInfo: WeatherInfo)
    fun saveBitmap(city: String, picture: Bitmap)
    fun loadBitmap(city: String): Observable<Bitmap>

}