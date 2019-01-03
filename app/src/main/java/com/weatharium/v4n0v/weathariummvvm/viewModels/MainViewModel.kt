package com.weatharium.v4n0v.weathariummvvm.viewModels

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.graphics.Bitmap
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.api.ImageApi
import com.weatharium.v4n0v.weathariummvvm.components.IImageLoader
import com.weatharium.v4n0v.weathariummvvm.components.random
import com.weatharium.v4n0v.weathariummvvm.model.WeatherInfo
import com.weatharium.v4n0v.weathariummvvm.model.images.Photos
import com.weatharium.v4n0v.weathariummvvm.repositories.IWeatherRepo
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel : ViewModel() {
    val fabVisibility = ObservableField(false)
    val isPhotoLoaded = ObservableField(true)


    val cityBitmapData: MutableLiveData<Bitmap> by lazy { MutableLiveData<Bitmap>() }
    val weatherInfoData: MutableLiveData<WeatherInfo> by lazy { MutableLiveData<WeatherInfo>() }
    val clickListener: MutableLiveData<Event<String>> by lazy { MutableLiveData<Event<String>>() }

    val ubdateButtonData:MutableLiveData<Event<String>> by lazy { MutableLiveData<Event<String>>()  }
    val navigateToDetails : LiveData<Event<String>>
        get() = ubdateButtonData

    @Inject
    lateinit var repository: IWeatherRepo


    @Inject
    lateinit var glideImageLoader: IImageLoader

    @SuppressLint("CheckResult")
    fun loadPhoto() {
        isPhotoLoaded.set(false)
        repository.loadCity().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { city ->

                    repository.loadWeather(city).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe { weatherInfo ->
                                weatherInfoData.value = weatherInfo
                            }

//                    val d: Observable<WeatherInfo> = Observable.create { e -> repository.loadWeather("Moscow", e) }
//                    d.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe { weatherInfo ->
//                        weatherInfoData.value=weatherInfo
//                    }

                    fabVisibility.set(true)

                    repository.loadBitmap(city).subscribeOn(Schedulers.computation())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe { bmp ->
                                cityBitmapData.value = bmp
                                isPhotoLoaded.set(true)

                            }

                }
    }
    @SuppressLint("CheckResult")
    fun downLoadPhoto() {
        isPhotoLoaded.set(false)
        repository.loadCity().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {city->
                    getPhotos(city ?: "Moscow")
                            ?.subscribe { protos ->
                                val index = random(0, protos.photos.count-1)
                                glideImageLoader.downloadPhoto(city
                                        ?: "Moscow", protos.photos.photo[index].urlM) { bmp ->
                                    cityBitmapData.value = bmp
                                    isPhotoLoaded.set(true)
                                }
                            }
                }
    }


      fun addClickEvent(event:String){
        clickListener.value = Event(event)
    }

    fun fabClick(){
        addClickEvent("FAB")

    }

//todo общее
    private fun getPhotos(text: String?): Observable<Photos>? {
        return App.instance.getService(ImageApi::class.java).getImage(SplashViewModel.FLIKR_KEY, SplashViewModel.IMAGE_SIZE, SplashViewModel.IMAGE_COUNT, "$text+${SplashViewModel.FLIKR_REQUEST_IMAGE_KEY}")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }


}