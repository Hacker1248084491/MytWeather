package com.myt.mytweather.logic.network

import com.myt.mytweather.MytWeatherApplication
import com.myt.mytweather.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/***
 * 网络请求接口
 */

interface PlaceService {

    //查询相关城市
    @GET("v2/place?token=${MytWeatherApplication.TOKEN}&lang=zh_CN")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>

}