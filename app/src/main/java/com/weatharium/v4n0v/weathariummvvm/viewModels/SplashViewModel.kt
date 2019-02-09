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
    val fabVisibility = ObservableField(false)

    init {
        WeatherBus.bus.register(this)
    }

    @Inject
    lateinit var repository: IWeatherRepo


    val isSplashCompleteData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val cityBitmapData: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }

    val isPhotoLoaded = ObservableField(true)

    @Inject
    lateinit var imageRepository: IImageCacheRepo
    var city: String? = null


    fun loadCityName() {
        isLoading.set(true)
        repository.loadCity()
                .subscribe { cityName ->
                    WeatherBus.bus.post("Moscow")
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
                            city = weather.name
                            cityBitmapData.value = bmp
                            isLoading.set(false)
                            isSplashCompleteData.value = true
                            fabVisibility.set(true)
                        }
                    }
                }
    }
}

