package com.weatharium.v4n0v.weathariummvvm.di.modules

import com.weatharium.v4n0v.weathariummvvm.components.GlideImageLoader
import com.weatharium.v4n0v.weathariummvvm.components.IImageLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Singleton
@Module
// @Module(includes = [AppModule::class])
class ImageModule {

    @Singleton
    @Provides
    fun glideLoader(): IImageLoader {
        return GlideImageLoader()
    }

}