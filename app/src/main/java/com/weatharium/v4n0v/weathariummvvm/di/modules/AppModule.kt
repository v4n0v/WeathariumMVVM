package com.weatharium.v4n0v.weathariummvvm.di.modules

import com.weatharium.v4n0v.weathariummvvm.App
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: App) {

    @Provides
    internal fun app(): App {
        return app
    }
}
