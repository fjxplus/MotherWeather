package com.motherweather.android

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.motherweather.android.databinding.ActivityWeatherBinding
import com.motherweather.android.logic.model.Weather
import com.motherweather.android.logic.model.getSky
import com.motherweather.android.ui.weather.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }

    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //将系统状态栏适应背景
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)         // 通知视窗，我们（应用）会处理任何系统视窗（而不是 decor）
            //WindowCompat.setDecorFitsSystemWindows(window, false)         // 或者您可以使用 AndroidX v1.5.0-alpha02 中的 WindowCompat
        }

        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (viewModel.locationLng.isEmpty()) {          //用ViewModel保存城市信息，防止屏幕旋转数据丢失
            viewModel.locationLng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationLat.isEmpty()) {
            viewModel.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }

        viewModel.weatherLiveData.observe(this, Observer { result ->        //监听ViewModel数据变化回调
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)            //获取到Weather则进行显示，更新UI组件
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
        viewModel.refreshWeather(
            viewModel.locationLng,
            viewModel.locationLat
        )          //向ViewModel层请求数据
    }

    private fun showWeatherInfo(weather: Weather) {
        binding.apply {
            val realtime = weather.realtime
            val daily = weather.daily

            //填充now.xml
            now.apply {
                placeName.text = viewModel.placeName
                val currentTempText = "${realtime.temperature.toInt()} ℃"
                currentTemp.text = currentTempText
                currentSky.text = getSky(realtime.skyCon).info
                val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
                currentAQI.text = currentPM25Text
                nowLayout.setBackgroundResource(getSky(realtime.skyCon).bg)
            }
            //填充forecast.xml
            forecast.apply {
                forecastLayout.removeAllViews()
                val days = daily.skycon.size
                for (i in 0 until days) {           //根据服务器返回的天数，创建预报的子布局并加载到forecastLayout中
                    val skycon = daily.skycon[i]
                    val temperature = daily.temperature[i]
                    val view = LayoutInflater.from(this@WeatherActivity)
                        .inflate(R.layout.forecast_item, forecastLayout, false)
                    val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
                    val skyIcon = view.findViewById<ImageView>(R.id.skyIcon)
                    val skyInfo = view.findViewById<TextView>(R.id.skyInfo)
                    val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
                    val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    dateInfo.text = simpleDateFormat.format(skycon.date)
                    val sky = getSky(skycon.value)
                    skyIcon.setImageResource(sky.icon)
                    skyInfo.text = sky.info
                    val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
                    temperatureInfo.text = tempText
                    forecastLayout.addView(view)
                }
            }
            //填充life_index.xml
            lifeIndex.apply {
                val lifeIndex = daily.lifeIndex
                coldRiskText.text = lifeIndex.coldRisk[0].desc
                dressingText.text = lifeIndex.dressing[0].desc
                ultravioletText.text = lifeIndex.ultraviolet[0].desc
                comfortText.text = lifeIndex.comfort[0].desc
            }

            weatherLayout.visibility = View.VISIBLE         //使布局可见

        }
    }
}