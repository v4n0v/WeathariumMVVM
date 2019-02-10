package com.weatharium.v4n0v.weathariummvvm.viewModels

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.graphics.Bitmap
import com.squareup.otto.Subscribe
import com.weatharium.v4n0v.weathariummvvm.components.channels.WeatherBus
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import com.weatharium.v4n0v.weathariummvvm.repositories.weather.IWeatherRepo
import com.weatharium.v4n0v.weathariummvvm.repositories.images.IImageCacheRepo
import timber.log.Timber
import javax.inject.Inject

@SuppressLint("CheckResult")
class SplashViewModel : ViewModel() {
    val isLoading = ObservableField(true)

    init {
        WeatherBus.bus.register(this)
    }

    @Inject
    lateinit var repository: IWeatherRepo

    val cityNameData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val errorData: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }

    @Inject
    lateinit var imageRepository: IImageCacheRepo



    fun loadCityName() {
        isLoading.set(true)
        repository.loadCity()
                .subscribe { cityName ->
                    WeatherBus.bus.post(cityName)
                }
    }

    override fun onCleared() {
        WeatherBus.bus.unregister(this)
    }

    @Subscribe
    fun onRecieve(weather: WeatherInfo) {
        repository.saveWeather(weather.name, weather)
        Timber.d("${weather.name}, temp = ${weather.main.temp}")
        val strNoSpaces = weather.name.replace(" ", "+")
        imageRepository.getPhotosFromFlickr(strNoSpaces)
                .subscribe { photos ->
                    photos?.let {
                        Timber.d("Images count ${it.photos.count}")
                        imageRepository.loadPicture(weather.name) { bmp ->
                            repository.saveCity(weather.name)
                            repository.saveWeather(weather.name, weather)
                            cityNameData.value=weather.name

                            isLoading.set(false)

                        }
                    }
                }
    }



    @Subscribe
    fun onErrorRecieve(error: Throwable) {
        errorData.value = error
    }
}

