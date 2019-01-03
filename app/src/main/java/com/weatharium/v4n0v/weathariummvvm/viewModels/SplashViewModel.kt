package com.weatharium.v4n0v.weathariummvvm.viewModels

import android.annotation.SuppressLint
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.graphics.Bitmap
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.api.ImageApi
import com.weatharium.v4n0v.weathariummvvm.api.WeatherApi
import com.weatharium.v4n0v.weathariummvvm.components.IImageLoader
import com.weatharium.v4n0v.weathariummvvm.components.KEY
import com.weatharium.v4n0v.weathariummvvm.components.random
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import com.weatharium.v4n0v.weathariummvvm.model.images.Photos
import com.weatharium.v4n0v.weathariummvvm.repositories.IWeatherRepo
import com.weatharium.v4n0v.weathariummvvm.repositories.PaperRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

class SplashViewModel : ViewModel() {
    val isLoading = ObservableField(true)
    val fabVisibility = ObservableField(false)

    @Inject
    lateinit var repository: IWeatherRepo

    val isSplashCompleteData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }
    val cityBitmapData: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }

    val isPhotoLoaded = ObservableField(true)

    @Inject
    lateinit var glideImageLoader: IImageLoader

    companion object {
          const val FLIKR_KEY = "9c6b4a5f6ad93dafa5a5ca0ef3b2f864"
          const val IMAGE_COUNT = 50
          const val IMAGE_SIZE = "url_m"
          const val FLIKR_REQUEST_IMAGE_KEY = "city"
    }

      var city: String? = null


    @SuppressLint("CheckResult")
    fun loadCityName() {
        isLoading.set(true)
        repository.loadCity()
                .subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe { cityName ->
                    getWeather(cityName)?.subscribe { weather ->
                        Timber.d("${weather.name}, temp = ${weather.main.temp}")
                        val strNoSpaces = weather?.name?.replace(" ", "+")
                        getPhotos(strNoSpaces)
                                ?.subscribe {
                                    Timber.d("Images count ${it.photos.count}")
                                    val index = random(0, it.photos.count-1)
                                    glideImageLoader.loadPicture(weather.name, it.photos.photo[index].urlM) { bmp ->
                                        repository.saveCity(weather.name)
                                        repository.saveWeather(weather.name, weather)
                                        repository.saveBitmap(weather.name, bmp!!)
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



    private fun getWeather(city: String): Observable<WeatherInfo>? {
        return App.instance.getService(WeatherApi::class.java).getWeather(city, KEY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())


    }

    private fun getPhotos(text: String?): Observable<Photos>? {
        return App.instance.getService(ImageApi::class.java).getImage(FLIKR_KEY, IMAGE_SIZE, IMAGE_COUNT, "$text+$FLIKR_REQUEST_IMAGE_KEY")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


}

