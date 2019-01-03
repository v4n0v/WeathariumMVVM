package com.weatharium.v4n0v.weathariummvvm.repositories

import android.graphics.Bitmap
import com.weatharium.v4n0v.weathariummvvm.components.BOOK_BMP
import com.weatharium.v4n0v.weathariummvvm.components.BOOK_CITY
import com.weatharium.v4n0v.weathariummvvm.components.BOOK_WEATWER
import com.weatharium.v4n0v.weathariummvvm.components.PAGE_CITY
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import io.paperdb.Paper
import io.reactivex.Observable
import io.reactivex.ObservableEmitter


class PaperRepository : IWeatherRepo {
    override fun saveBitmap(city: String, picture: Bitmap) {
        Paper.book(BOOK_BMP).write(city, picture)
    }

    override fun loadBitmap(city: String): Observable<Bitmap> {
        return Observable.fromCallable {
         val bmp =   Paper.book(BOOK_BMP).read<Bitmap>(city)
            bmp
        }
    }

    override fun loadCity(): Observable<String> {
        return Observable.fromCallable {
            Paper.book(BOOK_CITY).read(PAGE_CITY, "Moscow")
        }
    }

    override fun saveCity(city: String) {
        Paper.book(BOOK_CITY).write(PAGE_CITY, city)
    }
//
    override fun loadWeather(city: String): Observable<WeatherInfo>  {
        return Observable.fromCallable {
            Paper.book(BOOK_WEATWER).read<WeatherInfo>(city)

        }
    }

//    override  fun loadWeather(city: String, e: ObservableEmitter<WeatherInfo>) {
//        val weather = Paper.book(BOOK_WEATWER).read<WeatherInfo>(city)
//        e.onNext(weather)
//        e.onComplete()
//    }

    override fun saveWeather(city: String, weatherInfo: WeatherInfo) {
        Paper.book(BOOK_WEATWER).write(city, weatherInfo)
    }

}
