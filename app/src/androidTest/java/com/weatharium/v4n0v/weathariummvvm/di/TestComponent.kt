package com.weatharium.v4n0v.weathariummvvm.di

import com.weatharium.v4n0v.weathariummvvm.WeathariumInstumentalTest
import com.weatharium.v4n0v.weathariummvvm.di.modules.ImageRepoModule
import com.weatharium.v4n0v.weathariummvvm.di.modules.WeatherRepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    WeatherRepositoryModule::class,
    ImageRepoModule::class])
interface TestComponent {
    fun inject(test: WeathariumInstumentalTest)
}