package com.motherweather.android.logic

import androidx.lifecycle.liveData
import com.motherweather.android.logic.dao.PlaceDao
import com.motherweather.android.logic.model.Place
import com.motherweather.android.logic.model.Weather
import com.motherweather.android.logic.network.MotherWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.Exception
import kotlin.RuntimeException
import kotlin.coroutines.CoroutineContext

/**
 * 仓库层，判断调用方请求的数据应该是从本地数据源中获取还是从网络数据源中获取，并将获得的数据返回给调用方
 * 向ViewModel层提供从网络获取数据的接口
 * 返回LiveData数据用于监听
 */
object Repository {

    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val responseData = MotherWeatherNetwork.searchPlaces(query)         //从网络层获取数据
        if (responseData.status == "ok") {           //判断是否获取成功
            val places = responseData.places
            Result.success(places)              //将结果封装
        } else {
            Result.failure(RuntimeException("response status is ${responseData.status}"))           //失败则封装异常
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {          //使用async并发执行
                MotherWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                MotherWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()         //当两个请求全部完成之狗才往下执行
            val dailyResponse = deferredDaily.await()
            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather =
                    Weather(dailyResponse.result.daily, realtimeResponse.result.realtime)
                Result.success(weather)
            } else {
                Result.failure(
                    RuntimeException(
                        "realtime response status is ${realtimeResponse.status}" +
                                "daily response status is ${dailyResponse.status}"
                    )
                )
            }
        }
    }

    /**
     * 高阶函数liveData()自动创建LiveData对象，使用emit()将数据弹出，相当于setValue()方法，
     * 拥有挂起函数上下文，参数指定为Dispatchers.IO代码块会在子线程中运行,
     */
    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure(e)
            }
            emit(result)            //弹出数据，自动创建LiveData数据
        }

    /**
     * 仓库层获取本地保存城市信息的接口
     */
    fun savePlace(place: Place) = PlaceDao.savePlace(place)

    fun getSavedPlace() = PlaceDao.getSavedPlace()

    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

}