package com.weatharium.v4n0v.weathariummvvm.di.modules

import com.weatharium.v4n0v.weathariummvvm.repositories.IWeatherRepo
import com.weatharium.v4n0v.weathariummvvm.repositories.PaperRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Module
class WeatherCacheModule {

    @Singleton
    @Provides
    fun getWeatherCache(@Named("paper")cache: IWeatherRepo): IWeatherRepo {
        return cache
    }


    @Provides
    @Named("paper")
    fun getPaperCache():IWeatherRepo{
        return PaperRepository()
    }

}