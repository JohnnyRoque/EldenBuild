package com.eldenbuild.application

import android.app.Application

class EldenBuildApplication: Application() {
   private lateinit var _container :AppDataContainer
    val container get() = _container

    override fun onCreate() {
        super.onCreate()
        _container = AppDataContainer(this)
    }
}