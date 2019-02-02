//package com.weatharium.v4n0v.weathariummvvm.di.modules
//
//import com.weatharium.v4n0v.weathariummvvm.components.GlideImageLoader
//import com.weatharium.v4n0v.weathariummvvm.components.IImageLoader
//import com.weatharium.v4n0v.weathariummvvm.repositories.images.IImageCacheRepo
//import com.weatharium.v4n0v.weathariummvvm.repositories.images.PaperImagesCache
//import dagger.Module
//import dagger.Provides
//import javax.inject.Singleton
//
//@Singleton
//@Module
//(includes = [ImageRepoModule::class])
//class ImageModule {
//
//    @Singleton
//    @Provides
//    fun glideLoader(cache: IImageCacheRepo): IImageLoader {
//        return GlideImageLoader(cache)
//    }
//
//}