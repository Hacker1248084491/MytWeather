package com.myt.mytweather.logic.model

import com.google.gson.annotations.SerializedName

/***
 * model，数据放回解析对象
 */

data class PlaceResponse(val status: String, val places: List<Place>)
data class Place(val name: String, val location: Location, @SerializedName("formatted_address") val address: String)
data class Location(val lng: Double, val lat: Double)