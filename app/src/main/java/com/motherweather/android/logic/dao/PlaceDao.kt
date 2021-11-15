package com.motherweather.android.logic.dao

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import com.motherweather.android.MotherWeatherApplication
import com.motherweather.android.logic.model.Place

/**
 * 访问保存Place的SharePreference数据库接口
 */
object PlaceDao {

    /**
     * 保存城市
     */
    fun savePlace(place: Place) {
        sharedPreference().edit {
            putString("place", Gson().toJson(place))
        }
    }

    /**
     * 读取存储的城市
     */
    fun getSavedPlace(): Place {
        val place = sharedPreference().getString("place", "")
        return Gson().fromJson(place, Place::class.java)
    }

    /**
     * 判断数据库中是否保存有城市
     */
    fun isPlaceSaved() = sharedPreference().contains("place")

    /**
     * 获取上下文并获取SharePreference数据库
     */
    private fun sharedPreference() =
        MotherWeatherApplication.context.getSharedPreferences(
            "mother_weather",
            Context.MODE_PRIVATE
        )

}