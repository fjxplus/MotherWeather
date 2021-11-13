package com.motherweather.android.logic.network

import com.motherweather.android.MotherWeatherApplication
import com.motherweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 访问彩云天气访问城市搜索API的Retrofit接口
 */
interface PlaceService {

    /**
     * @param(query: String) 提供访问url地址中的query参数
     * @return(Call<PlaceResponse>)
     */
    @GET("v2/place?token={MotherWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query")query: String):Call<PlaceResponse>

}