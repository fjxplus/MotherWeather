package com.motherweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.motherweather.android.logic.Repository
import com.motherweather.android.logic.model.Place

/**
 * 用于保存城市信息的ViewModel
 */
class PlaceViewModel: ViewModel() {

    private val searchLiveData = MutableLiveData<String>()          //保存城市名称

    val placeList = ArrayList<Place>()

    val placeLiveData = Transformations.switchMap(searchLiveData){query ->
        Repository.searchPlaces(query)          //从仓库中获取包含城市信息的liveData，并用于监听数据变化
    }

    fun searchPlaces(query: String){            //向UI控制层提供获取数据的接口，传入参数是liveData数据发生变化，触发switchMap()
        searchLiveData.value = query
    }

    fun savePlace(place: Place) = Repository.savePlace(place)           //用于向仓库层查询数据库的接口

    fun getSavedPlace() = Repository.getSavedPlace()

    fun isPlaceSaved() = Repository.isPlaceSaved()
}