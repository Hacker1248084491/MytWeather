package com.myt.mytweather.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object MytWeatherNetwork {

    //创建网络访问动态代理对象
    private val placeService = ServiceCreator.create<PlaceService>()

    //统一封装数据返回回调
    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T>{
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    /**
     * 实际网络请求方法
     */

    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

}