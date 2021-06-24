package com.san.simpletaxiapp.utils

import android.app.Application
import com.san.simpletaxiapp.viewmodels.MyViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@MyApp)
            loadKoinModules(listOf(viewModelModule))
        }
    }

}

val viewModelModule = module {
    viewModel {
        MyViewModel()
    }
}
