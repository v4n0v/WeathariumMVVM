package com.weatharium.v4n0v.weathariummvvm.di

import com.weatharium.v4n0v.weathariummvvm.components.IImageLoader
import com.weatharium.v4n0v.weathariummvvm.di.modules.AppModule
import com.weatharium.v4n0v.weathariummvvm.di.modules.ImageModule
import com.weatharium.v4n0v.weathariummvvm.di.modules.WeatherCacheModule
import com.weatharium.v4n0v.weathariummvvm.viewModels.MainViewModel
import com.weatharium.v4n0v.weathariummvvm.viewModels.SplashViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ImageModule::class, AppModule::class, WeatherCacheModule::class])
interface AppComponent  {
    fun inject(imageLoader: IImageLoader)
    fun inject(viewModel: SplashViewModel)
    fun inject(viewModel: MainViewModel)
}