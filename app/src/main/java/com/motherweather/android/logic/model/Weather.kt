package com.motherweather.android.logic.model

data class Weather(val daily: DailyResponse.Daily, val realtime: RealtimeResponse.Realtime)