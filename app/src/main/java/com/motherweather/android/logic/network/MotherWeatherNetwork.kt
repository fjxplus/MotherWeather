package com.motherweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 网络数据源访问接口
 * search()方法使用获得的动态代理对象实例调用接口方法，同时声明为挂起函数
 * 为泛型Call<T>定义await()方法，使用suspendCoroutine将协程阻塞并执行网络请求回调
 */
object MotherWeatherNetwork {

    private val placeService = ServiceCreator.create<PlaceService>()

    /**
     * searchPlaces()方法用于发出请求
     */
    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object: Callback<T>{

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)         //恢复被挂起的协程，并传入服务器返回的数据
                    else continuation.resumeWithException(              //恢复被挂起的协程，并抛出异常
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }


}