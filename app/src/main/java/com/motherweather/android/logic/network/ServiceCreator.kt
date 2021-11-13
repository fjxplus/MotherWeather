package com.motherweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Retrofit构建器，创建retrofit实例
 * create()方法返回接口的动态代理接口对象
 */
object ServiceCreator {

    private const val BASE_URL = "https://api.caiyunapp.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)       //获取动态代理对象

    inline fun <reified T> create(): T = create(T::class.java)      //泛型实化

}