package com.eldenbuild.application

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class EldenBuildApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@EldenBuildApplication)
            modules(appModule)
        }
    }
}

class TestEldenBuildApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TestEldenBuildApplication)
            modules(appModule,instrumentedTestModule)
        }
    }
}


