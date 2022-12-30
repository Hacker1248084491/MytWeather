package com.myt.mytweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MytWeatherApplication: Application(){

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        //彩云天气token
        const val TOKEN = "ywAsbrCtQuJiFRmV"
    }

}