package com.weatharium.v4n0v.weathariummvvm.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiFactory(val gson: GsonConverterFactory,
                 private val client: OkHttpClient,
                 private val weatherUrl: String,
                 private val imageURl: String) {

    private inline fun <reified T> getApi(baseUrl: String): T {
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gson)
                .build()
        return retrofit.create(T::class.java)
    }

    fun getWeatherApi(): WeatherApi = getApi(weatherUrl)
    fun getImageApi(): ImageApi = getApi(imageURl)

}