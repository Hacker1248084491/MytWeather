package com.myt.mytweather.logic

import androidx.lifecycle.liveData
import com.myt.mytweather.logic.dao.PlaceDao
import com.myt.mytweather.logic.model.Place
import com.myt.mytweather.logic.model.Weather
import com.myt.mytweather.logic.network.MytWeatherNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

/***
 * 仓库层，用来进一步处理网络或者本地数据(例如对于网络数据是否使用缓存)
 */
object Repository {

    //查询数据，使用livedata来返回数据，并指定线程
    fun searchPlaces(query: String) = fire(Dispatchers.IO) {
        val placeResponse = MytWeatherNetwork.searchPlaces(query)
        if(placeResponse.status == "ok"){
            val places = placeResponse.places
            Result.success(places)
        }else{
            Result.failure(RuntimeException("response status is${placeResponse.status}"))
        }
    }

    fun refreshWeather(lng: String, lat: String) = fire(Dispatchers.IO) {
        coroutineScope {
            val deferredRealtime = async {
                MytWeatherNetwork.getRealtimeWeather(lng, lat)
            }
            val deferredDaily = async {
                MytWeatherNetwork.getDailyWeather(lng, lat)
            }
            val realtimeResponse = deferredRealtime.await()
            val dailyResponse = deferredDaily.await()

            if (realtimeResponse.status == "ok" && dailyResponse.status == "ok") {
                val weather = Weather(realtimeResponse.result.realtime,
                    dailyResponse.result.daily)
                Result.success(weather)
            }else{
                Result.failure(RuntimeException("realtime response status is ${realtimeResponse.status}" + "daily response status is ${dailyResponse.status}"))
            }
        }
    }

    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) = liveData(context) {
        val result = try {
            block()
        }catch (e: Exception){
            Result.failure(e)
        }
        emit(result)
    }

    fun savePlace(place: Place) = PlaceDao.savePlace(place)
    fun getSavedPlace() = PlaceDao.getSavedPlace()
    fun isPlaceSaved() = PlaceDao.isPlaceSaved()

}