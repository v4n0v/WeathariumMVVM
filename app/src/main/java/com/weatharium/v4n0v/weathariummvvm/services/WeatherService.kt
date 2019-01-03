//package com.weatharium.v4n0v.weathariummvvm.services
//
//import android.app.Service
//import android.content.Intent
//import android.os.Binder
//import android.os.IBinder
//import android.util.Log
//import com.weatharium.v4n0v.weathariummvvm.App
//import com.weatharium.v4n0v.weathariummvvm.components.WeatherBus
//import com.weatharium.v4n0v.weathariummvvm.components.KEY
//import io.paperdb.Paper
//import rx.android.schedulers.AndroidSchedulers
//import rx.schedulers.Schedulers
//import timber.log.Timber
//import java.util.*
//
//
//class WeatherService : Service() {
//    override fun onBind(intent: Intent?): IBinder? {
//        return binder
//    }
//
//    private val interval: Long = 600000
//    private var timer: Timer? = null
//    private var tTask: TimerTask? = null
//    private val binder = WeatherBinder()
//    private var weatherInfoData: String? = null
//    private var isComplete: Boolean = false
//
//    override fun onCreate() {
//        super.onCreate()
//        WeatherBus.bus.register(this)
//        timer = Timer()
//        schedule()
//    }
//
//    fun changeCity(weatherInfoData: String) {
//        this.weatherInfoData = weatherInfoData
//        isComplete = false
//        schedule()
//    }
//
//    private fun schedule() {
//        if (weatherInfoData != null)
//
//
//
////        if (tTask != null) tTask!!.cancel()
////        // начинаем отсчет до 10минут
////        Timber.d("Service weather interval $interval")
////        tTask = object : TimerTask() {
////            override fun run() {
////                if (weatherInfoData != null) {
////                    if (!isComplete) {
////                        App.instance?.service?.getWeather(weatherInfoData, KEY)?.subscribeOn(Schedulers.io())
////                                ?.observeOn(AndroidSchedulers.mainThread())
////                                ?.repeat(20000)
////                                ?.doOnError {
////                                    Log.d("RetrofitRequest", it.message)
////                                }
////                                ?.subscribe { weather ->
////                                     WeatherBus.bus.post(weather)
////                                    Paper.book("weatherInfoData").write("weatherInfoData", weather.name)
////                                    Timber.d("${weather.name}, temp = ${weather.main.temp}")
////                                }
////                    }
////                }
////            }
////        }
////        timer!!.schedule(tTask, 1000, interval)
//    }
//
//    inner class WeatherBinder : Binder() {
//        val service: WeatherService
//            get() = this@WeatherService
//    }
//}
