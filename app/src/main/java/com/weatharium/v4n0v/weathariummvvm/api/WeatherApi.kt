package com.weatharium.v4n0v.weathariummvvm.api


import com.weatharium.v4n0v.weathariummvvm.components.URL_WEATHER
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

@BaseUrl(URL_WEATHER)
interface WeatherApi {
    @GET("weather?units=metric")
    fun getWeather(@Query("q") city: String?,
                   @Query("appid") key: String): Observable<WeatherInfo>
}