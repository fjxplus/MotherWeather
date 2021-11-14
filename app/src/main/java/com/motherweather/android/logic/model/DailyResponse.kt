package com.motherweather.android.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class DailyResponse(val status: String, val result: Result) {

    data class Result(val daily: Daily, val primary: Int)

    data class Daily(
        @SerializedName("life_index")val lifeIndex: LifeIndex,
        val skycon: List<Skycon>,
        val temperature: List<Temperature>
    )

    data class Temperature(val max: Double, val min: Double)

    data class Skycon(val date: Date, val value: String)

    data class LifeIndex(
        val comfort: List<LifeDescription>,
        val coldRisk: List<LifeDescription>,
        val dressing: List<LifeDescription>,
        val ultraviolet: List<LifeDescription>
    )

    data class LifeDescription(val date: String, val desc: String, val index: String)
}