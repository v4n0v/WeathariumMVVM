package com.weatharium.v4n0v.weathariummvvm.di.modules

import com.weatharium.v4n0v.weathariummvvm.api.ApiFactory
import com.weatharium.v4n0v.weathariummvvm.repositories.IWeatherRepo
import com.weatharium.v4n0v.weathariummvvm.repositories.PaperWeatherRepository
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Module(includes = [ApiFactoryModule::class])
class WeatherRepositoryModule {


    @Singleton
    @Provides
    fun getWeatherCache(@Named("paper") cache: IWeatherRepo): IWeatherRepo {
        return cache
    }

    @Provides
    @Named("paper")
    fun getPaperCache(apiFactory:ApiFactory): IWeatherRepo {
        return PaperWeatherRepository(apiFactory)
    }
}