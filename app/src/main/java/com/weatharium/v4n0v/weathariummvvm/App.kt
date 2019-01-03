package com.weatharium.v4n0v.weathariummvvm

import android.app.Application
import com.google.gson.GsonBuilder
import com.weatharium.v4n0v.weathariummvvm.api.BaseUrl
import com.weatharium.v4n0v.weathariummvvm.di.AppComponent
import com.weatharium.v4n0v.weathariummvvm.di.DaggerAppComponent
import com.weatharium.v4n0v.weathariummvvm.di.modules.AppModule
import com.weatharium.v4n0v.weathariummvvm.di.modules.ImageModule
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import io.paperdb.Paper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


class App : Application() {

    private val client by lazy {
        val c = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Timber.d(it)
        })
        logging.level = HttpLoggingInterceptor.Level.BASIC

        c.addInterceptor(logging)
//        c.addInterceptor { chain ->
//            val response: Response
//
//            val newUrl: HttpUrl? = chain.request().url()
//
//            var request = chain.request()
//                    .newBuilder()
//                    .addHeader("Connection", "keep-alive")
//                    .url(Objects.requireNonNull<HttpUrl>(newUrl))
//                    .build()
//
//            response = chain.proceed(request)
//            val bt = response.body()
//         //   val body = response.body()?.string()
//
//            response
//        }
        c
    }


    fun getAppComponent(): AppComponent {
        return appComponent
    }

    private lateinit var appComponent: AppComponent
    private val gson by lazy {
        GsonBuilder()
                .setLenient()
                // .registerTypeAdapter(Photos::class.java, PhotoDeserializer())
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
        Paper.init(this)
        //Realm.init(this)
//        val config = RealmConfiguration.Builder()
//                .deleteRealmIfMigrationNeeded()
//                .build()
//
//        Realm.setDefaultConfiguration(config)
//
        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .imageModule(ImageModule())
                .build()

//        client.addInterceptor { chain ->
//            val original = chain.request()
//
//            // Настраиваем запросы
//            val request = original.newBuilder()
////                    .header("Accept", "application/json")
////                    .header("Authorization", "auth-token")
//                    .method(original.method(), original.body())
//                    .build()
//            Timber.d("(${request.method().toUpperCase()}) ${request.url()}")
//
//            chain.proceed(request)
//        }


    }

    fun <T> getService(api: Class<T>): T {
        val baseUrl = api.getAnnotation(BaseUrl::class.java)
        val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl.value)
                .client(client.build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        return retrofit.create(api)
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
