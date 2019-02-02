package com.weatharium.v4n0v.weathariummvvm.di.modules

import com.weatharium.v4n0v.weathariummvvm.App
import com.weatharium.v4n0v.weathariummvvm.api.ApiFactory
import com.weatharium.v4n0v.weathariummvvm.repositories.images.IImageCacheRepo
import com.weatharium.v4n0v.weathariummvvm.repositories.images.PaperImagesCache
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [ApiFactoryModule::class, AppModule::class])
class ImageRepoModule {
    @Singleton
    @Provides
    fun imageRepo( @Named("paper") repository: IImageCacheRepo): IImageCacheRepo {
        return repository
    }

    @Provides
    @Named("paper")
    fun getPaperImageRepo(apiFactory: ApiFactory, app: App): IImageCacheRepo {
        return PaperImagesCache(apiFactory, app)
    }

}
