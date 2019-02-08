package com.weatharium.v4n0v.weathariummvvm.repositories.weather

import com.weatharium.v4n0v.weathariummvvm.api.ApiFactory
import com.weatharium.v4n0v.weathariummvvm.components.*
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import io.paperdb.Paper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*


class PaperWeatherRepository(private val apiFactory: ApiFactory) : IWeatherRepo {
    override fun loadWeatherHistory(): Observable<HashMap<String, WeatherInfo>> {
        return Observable.fromCallable {
            Paper.book(BOOK_WEATWER).read<HashMap<String, WeatherInfo>>(PAGE_HISTORY, null)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun downLoadWeather(city: String): Observable<WeatherInfo> {
        return apiFactory.getWeatherApi().getWeather(city.toLowerCase(), KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map { it ->
                    it.time = Date()
                    it
                }
    }

    override fun loadCity(): Observable<String> {
        return Observable.fromCallable {
            val city =Paper.book(BOOK_CITY).read<String>(PAGE_CITY, "Moscow")
            formatTitle(city)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveCity(city: String) {
        Paper.book(BOOK_CITY).write(PAGE_CITY, city.toLowerCase())
    }

    override fun loadWeather(city: String): Observable<WeatherInfo?> {
        return Observable.fromCallable {
            val list = Paper.book(BOOK_WEATWER).read<HashMap<String, WeatherInfo>>(PAGE_HISTORY, null)
            list[city.toLowerCase()]
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun saveWeather(city: String, weatherInfo: WeatherInfo) {
        val list = Paper.book(BOOK_WEATWER).read<HashMap<String, WeatherInfo>>(PAGE_HISTORY, null)
                ?: HashMap()
        list[city.toLowerCase()] = weatherInfo
        Paper.book(BOOK_WEATWER).write(PAGE_HISTORY, list)
    }
}
