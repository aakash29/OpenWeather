package com.open.weather.demo

import android.app.Application

class AppBase : Application() {

    companion object {

        private var instance: AppBase? = null

        fun applicationContext(): AppBase {
            return instance as AppBase
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}