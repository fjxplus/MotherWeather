package com.motherweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * MotherWeatherApplication类用于获取全局的Context，和彩云天气的令牌
 * 静态变量context
 * 静态常量TOKEN
 * 修改注册表<application>的android:name属性为.MotherWeatherApplication
 */
class MotherWeatherApplication:Application() {

    @SuppressLint("StaticFieldLeak")
    companion object{
        lateinit var context: Context
        const val TOKEN = "3JlxZTQp4u73wmMQ"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}