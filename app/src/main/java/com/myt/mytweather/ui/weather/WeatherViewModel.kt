package com.myt.mytweather.ui.weather

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.myt.mytweather.logic.Repository
import com.myt.mytweather.logic.model.Location

class WeatherViewModel: ViewModel(){

    private val locationLiveData = MutableLiveData<Location>()

    var locationLng = 0.0
    var locationLat = 0.0
    var placeName = ""

    val weatherLiveData = Transformations.switchMap(locationLiveData) { location ->
        Repository.refreshWeather(location.lng.toString(), location.lat.toString())
    }

    fun refreshWeather(lng: Double, lat: Double) {
        locationLiveData.value = Location(lng, lat)
    }



}