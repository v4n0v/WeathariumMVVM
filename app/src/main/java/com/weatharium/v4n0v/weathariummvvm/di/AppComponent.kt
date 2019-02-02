package com.weatharium.v4n0v.weathariummvvm.di

import com.weatharium.v4n0v.weathariummvvm.di.modules.AppModule
import com.weatharium.v4n0v.weathariummvvm.di.modules.ImageRepoModule
import com.weatharium.v4n0v.weathariummvvm.di.modules.WeatherRepositoryModule
import com.weatharium.v4n0v.weathariummvvm.services.WeatherService
import com.weatharium.v4n0v.weathariummvvm.viewModels.MainViewModel
import com.weatharium.v4n0v.weathariummvvm.viewModels.SplashViewModel
import dagger.Component
import javax.inject.Singleton
import android.app.Application
import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.viewModels.HistoryViewModel
import dagger.BindsInstance


@Singleton
@Component(modules = [
    //   ImageModule::class,
    AppModule::class,
    WeatherRepositoryModule::class,
    ImageRepoModule::class
])
interface AppComponent {
    fun inject(viewModel: SplashViewModel)
    fun inject(viewModel: MainViewModel)
    fun inject(viewModel: HistoryViewModel)
    fun inject(service: WeatherService)
}