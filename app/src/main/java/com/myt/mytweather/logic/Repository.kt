package com.myt.mytweather.logic

import androidx.lifecycle.liveData
import com.myt.mytweather.logic.network.MytWeatherNetwork
import kotlinx.coroutines.Dispatchers

/***
 * 仓库层，用来进一步处理网络或者本地数据(例如对于网络数据是否使用缓存)
 */
object Repository {

    //查询数据，使用livedata来返回数据，并指定线程
    fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
        val result = try {
            val placeResponse = MytWeatherNetwork.searchPlaces(query)
            if(placeResponse.status == "ok"){
                val places = placeResponse.places
                Result.success(places)
            }else{
                Result.failure(RuntimeException("response status is${placeResponse.status}"))
            }
        }catch (e: Exception){
            Result.failure(e)
        }
        //将result发送出去
        emit(result)
    }

}