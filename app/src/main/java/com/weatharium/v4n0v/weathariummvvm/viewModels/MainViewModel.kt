package com.weatharium.v4n0v.weathariummvvm.viewModels

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.graphics.Bitmap
import com.squareup.otto.Subscribe
import com.weatharium.v4n0v.weathariummvvm.activities.ClickEvent
import com.weatharium.v4n0v.weathariummvvm.components.channels.WeatherBus
import com.weatharium.v4n0v.weathariummvvm.components.random
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import com.weatharium.v4n0v.weathariummvvm.repositories.weather.IWeatherRepo
import com.weatharium.v4n0v.weathariummvvm.repositories.images.IImageCacheRepo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainViewModel : ViewModel() {
    val fabVisibility = ObservableField(false)
    val isPhotoLoaded = ObservableField(true)

    val errorData: MutableLiveData<Throwable> by lazy { MutableLiveData<Throwable>() }
    val cityNameData: MutableLiveData<String> by lazy { MutableLiveData<String>() }
    val cityBitmapData: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }
    val weatherInfoData: MutableLiveData<WeatherInfo> by lazy { MutableLiveData<WeatherInfo>() }
    val clickListener: MutableLiveData<Event<ClickEvent>> by lazy { MutableLiveData<Event<ClickEvent>>() }
    val ubdateButtonData: MutableLiveData<Event<String>> by lazy { MutableLiveData<Event<String>>() }

    val navigateToDetails: LiveData<Event<String>>
        get() = ubdateButtonData

    @Inject
    lateinit var repoWeather: IWeatherRepo

    @Inject
    lateinit var repoImages: IImageCacheRepo

    init {
        WeatherBus.bus.register(this)

    }

    override fun onCleared() {
        WeatherBus.bus.unregister(this)

    }

    @SuppressLint("CheckResult")
    fun loadPhoto() {
        isPhotoLoaded.set(false)
        repoWeather.loadCity().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { city ->
                    cityNameData.value = city
                    repoWeather.loadWeather(city).subscribe { weatherInfo ->
                        weatherInfoData.value = weatherInfo
                        fabVisibility.set(true)
                        repoImages.readFromCache(city).subscribe { file ->
                            file?.let {
                                repoImages.loadPictureFromPath(it) { bmp ->
                                    cityBitmapData.value = bmp
                                    isPhotoLoaded.set(true)
                                }
                            }
                        }
                    }
                }
    }

    @SuppressLint("CheckResult")
    fun downLoadPhoto() {
        isPhotoLoaded.set(false)
        repoWeather.loadCity().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { city ->
                    repoImages.getPhotosFromFlickr(city ?: "Moscow")
                            .subscribe { photos ->
                                photos?.let {
                                    val index = random(0, photos.photos.photo.size - 1)
                                    repoImages.downloadPhoto(city
                                            ?: "Moscow", photos.photos.photo[index].urlM) { bmp ->
                                        cityBitmapData.value = bmp
                                        isPhotoLoaded.set(true)
                                    }
                                }
                            }
                }
    }

    fun changeCity(city: String) {
        isPhotoLoaded.set(false)
        repoWeather.saveCity(city)
        WeatherBus.bus.post(city)
    }

    fun addClickEvent(event: ClickEvent) {
        clickListener.value = Event(event)
    }

    fun fabClick() {
        addClickEvent(ClickEvent.FAB)
    }

    @SuppressLint("CheckResult")
    @Subscribe
    fun onRecieve(weatherInfo: WeatherInfo) {
        repoWeather.saveWeather(weatherInfo.name, weatherInfo)
        repoImages.loadPicture(weatherInfo.name) { bmp ->
            weatherInfoData.value = weatherInfo
            cityNameData.value = weatherInfo.name

            cityBitmapData.value = bmp
            isPhotoLoaded.set(true)
        }
    }

    @Subscribe
    fun onErrorRecieve(error: Throwable) {
        errorData.value = error

    }



}