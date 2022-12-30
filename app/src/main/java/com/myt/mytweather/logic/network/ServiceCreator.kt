package com.myt.mytweather.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {

    //服务器跟地址
    private const val BASE_URL = "https://api.caiyunapp.com/"

    //创建Retrofit对象
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    //创建动态代理对象
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)

    //创建动态代理对象，重载，使用泛型实化
    inline fun <reified T> create(): T = create(T::class.java)

}