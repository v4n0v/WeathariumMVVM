package com.weatharium.v4n0v.weathariummvvm.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.squareup.otto.Subscribe
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.components.channels.WeatherBus
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import com.weatharium.v4n0v.weathariummvvm.repositories.weather.IWeatherRepo
import io.reactivex.observers.DisposableObserver
import timber.log.Timber
import java.util.*
import javax.inject.Inject


class WeatherService : Service() {
    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    private val interval: Long = 30_000
    private var timer: Timer? = null
    private var tTask: TimerTask? = null
    private val binder = WeatherBinder()
    private var city: String? = null
    private var isComplete: Boolean = false
    @Inject
    lateinit var repository: IWeatherRepo

    override fun onCreate() {
        super.onCreate()
        App.instance.getAppComponent().inject(this)
        WeatherBus.bus.register(this)
        timer = Timer()
        schedule()
    }

    override fun onDestroy() {
        WeatherBus.bus.unregister(this)
    }


    private fun schedule() {
        if (city != null && tTask != null)
            tTask?.cancel()
        // начинаем отсчет до 10минут
        Timber.d("Service weather interval $interval")
        tTask = object : TimerTask() {
            @SuppressLint("CheckResult")
            override fun run() {
                if (city != null) {
                    if (!isComplete) {
                        city?.let {
                            repository.downLoadWeather(it).subscribe(getObserver())
                        }
                    }
                }
            }
        }

        timer?.schedule(tTask, 0, interval)
    }

    inner class WeatherBinder : Binder() {
        val service: WeatherService
            get() = this@WeatherService
    }

    @Subscribe
    fun onReceive(city: String) {
        this.city = city
        isComplete = false
        schedule()
    }


    fun getObserver(): DisposableObserver<WeatherInfo> {
        return object : DisposableObserver<WeatherInfo>() {
            override fun onComplete() {
                Timber.d("Complete")
            }

            override fun onNext(weather: WeatherInfo) {
                Timber.d("onNext weatherInfo")
                WeatherBus.bus.post(weather)
            }

            override fun onError(e: Throwable) {
                Timber.e(e)
                e.printStackTrace()
            }
        }
    }
}
