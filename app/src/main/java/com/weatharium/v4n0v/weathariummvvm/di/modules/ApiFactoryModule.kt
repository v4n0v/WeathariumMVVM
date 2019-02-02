package com.weatharium.v4n0v.weathariummvvm.di.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.weatharium.v4n0v.weathariummvvm.api.ApiFactory
import com.weatharium.v4n0v.weathariummvvm.components.URL_IMAGES
import com.weatharium.v4n0v.weathariummvvm.components.URL_WEATHER
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import javax.inject.Named
import javax.inject.Singleton

@Module
 open class ApiFactoryModule {

    @Singleton
    @Provides
    fun apiFactory(@Named("client") client: OkHttpClient,
                   @Named("gson") gsonConverterFactory: GsonConverterFactory,
                   @Named("weather_url") weatherUrl: String,
                   @Named("image_url") imageURl: String): ApiFactory {
        return ApiFactory(gsonConverterFactory, client, weatherUrl, imageURl)
    }

    @Provides
    @Named("client")
    fun client(): OkHttpClient {
        val c = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.d(it)
        })
        logging.level = HttpLoggingInterceptor.Level.BASIC
        c.addInterceptor(logging)
        return c.build()
    }

    @Provides
    @Named("gson")
    fun gsonConverterFactory(gson: Gson) = GsonConverterFactory.create(gson)!!

    @Provides
    fun gson() = GsonBuilder()
            .setLenient()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
            .create()!!

    @Provides
    @Named("weather_url")
    open  fun weatherUrl() = URL_WEATHER

    @Provides
    @Named("image_url")
    open fun imageUrl() = URL_IMAGES
}