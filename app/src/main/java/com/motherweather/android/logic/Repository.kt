package com.motherweather.android.logic

import androidx.lifecycle.liveData
import com.motherweather.android.logic.model.Place
import com.motherweather.android.logic.model.PlaceResponse
import com.motherweather.android.logic.network.MotherWeatherNetwork
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.RuntimeException

/**
 * 仓库层，判断调用方请求的数据应该是从本地数据源中获取还是从网络数据源中获取，并将获得的数据返回给调用方
 * 向ViewModel层提供从网络获取数据的接口和liveData数据用于监听
 */
object Repository {

    //liveData()方法自动创建LiveData对象，使用emit()将数据弹出，相当于setValue()方法，参数指定为Dispatchers.IO代码块会在子线程中运行
    fun  searchPlaces(query: String) = liveData(Dispatchers.IO){
        val result = try{
            val responseData = MotherWeatherNetwork.searchPlaces(query)         //从网络层获取数据
            if (responseData.status == "ok"){           //判断是否获取成功
                val places = responseData.places
                Result.success(places)              //将结果封装
            }else{
                Result.failure(RuntimeException("response status is ${responseData.status}"))           //失败则封装异常
            }
        }catch (e :Exception){
            Result.failure(e)
        }
        emit(result)            //弹出数据，自动创建LiveData数据
    }

}