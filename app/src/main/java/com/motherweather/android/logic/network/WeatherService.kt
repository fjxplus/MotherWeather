package com.motherweather.android.logic.network

import com.motherweather.android.MotherWeatherApplication
import com.motherweather.android.logic.model.DailyResponse
import com.motherweather.android.logic.model.RealtimeResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 访问彩云天气获取天气的API接口
 */
interface WeatherService {

    @GET("v2.5/${MotherWeatherApplication.TOKEN}/{lng},{lat}/realtime.json")
    fun getRealtimeWeather(
        @Path("lng") lng: String,
        @Path("lat") lat: String
    ): Call<RealtimeResponse>

    @GET("v2.5/${MotherWeatherApplication.TOKEN}/{lng},{lat}/daily.json")
    fun getDailyWeather(@Path("lng") lng: String, @Path("lat") lat: String): Call<DailyResponse>

}