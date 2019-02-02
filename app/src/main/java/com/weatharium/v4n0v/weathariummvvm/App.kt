package com.weatharium.v4n0v.weathariummvvm

import android.app.Application
import com.weatharium.v4n0v.weathariummvvm.di.AppComponent
import com.weatharium.v4n0v.weathariummvvm.di.DaggerAppComponent
import com.weatharium.v4n0v.weathariummvvm.di.modules.AppModule
import io.paperdb.Paper
import timber.log.Timber


class App : Application() {
    fun getAppComponent(): AppComponent {
        return appComponent
    }

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        Timber.plant(Timber.DebugTree())
        Paper.init(this)

        appComponent = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .build()
    }


    companion object {
        lateinit var instance: App
            private set
    }
}
