package com.myt.mytweather.ui.weather

import android.content.Context
import android.graphics.Color
import android.hardware.input.InputManager
import android.inputmethodservice.InputMethodService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.myt.mytweather.R
import com.myt.mytweather.databinding.ActivityWeatherBinding
import com.myt.mytweather.databinding.ForecastItemBinding
import com.myt.mytweather.logic.model.Weather
import com.myt.mytweather.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {

    lateinit var activityWeatherBinding: ActivityWeatherBinding

    val viewModel by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWeatherBinding = ActivityWeatherBinding.inflate(layoutInflater)

        val decorView = window.decorView
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.statusBarColor = Color.TRANSPARENT

        setContentView(activityWeatherBinding.root)

        viewModel.locationLng = intent.getDoubleExtra("location_lng", 0.0)
        viewModel.locationLat = intent.getDoubleExtra("location_lat", 0.0)
        if (viewModel.placeName.isEmpty()) {
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        viewModel.weatherLiveData.observe(this){ result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                Toast.makeText(this, "无法成功获取天气信息", Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
            activityWeatherBinding.swipeRefresh.isRefreshing = false
        }

        activityWeatherBinding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        refreshWeather()
        activityWeatherBinding.swipeRefresh.setOnRefreshListener {
            refreshWeather()
        }

        activityWeatherBinding.nowRoot.navBtn.setOnClickListener {
            activityWeatherBinding.drawerLayout.openDrawer(GravityCompat.START)
        }
        activityWeatherBinding.drawerLayout.addDrawerListener(object : DrawerLayout.DrawerListener{
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {

            }

            override fun onDrawerClosed(drawerView: View) {
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(activityWeatherBinding.drawerLayout.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }

            override fun onDrawerStateChanged(newState: Int) {

            }

        })

    }

    fun refreshWeather() {
        viewModel.refreshWeather(viewModel.locationLng, viewModel.locationLat)
        activityWeatherBinding.swipeRefresh.isRefreshing = true
    }


    private fun showWeatherInfo(weather: Weather) {
        activityWeatherBinding.run {
            val realtime = weather.realtime
            val daily = weather.daily
            nowRoot.apply {
                placeName.text = viewModel.placeName
                // 填充now.xml布局中的数据
                val currentTempText = "${realtime.temperature.toInt()} ℃"
                currentTemp.text = currentTempText
                currentSky.text = getSky(realtime.skycon).info
                val currentPM25Text = "空气指数 ${realtime.airQuality.aqi.chn.toInt()}"
                currentAQI.text = currentPM25Text
                nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
            }
            // 填充forecast.xml布局中的数据
            forecastRoot.run {
                forecastLayout.removeAllViews()
                val days = daily.skycon.size
                for (i in 0 until days) {
                    val skycon = daily.skycon[i]
                    val temperature = daily.temperature[i]
                    val forecastItemBinding = ForecastItemBinding.inflate(layoutInflater, forecastLayout, false)
                    forecastItemBinding.run {
                        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                        dateInfo.text = simpleDateFormat.format(skycon.date)
                        val sky = getSky(skycon.value)
                        skyIcon.setImageResource(sky.icon)
                        skyInfo.text = sky.info
                        val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
                        temperatureInfo.text = tempText
                        forecastLayout.addView(forecastItemBinding.root)
                    }
                }
            }
            // 填充life_index.xml布局中的数据
            lifeIndexRoot.apply {
                val lifeIndex = daily.lifeIndex
                coldRiskText.text = lifeIndex.coldRisk[0].desc
                dressingText.text = lifeIndex.dressing[0].desc
                ultravioletText.text = lifeIndex.ultraviolet[0].desc
                carWashingText.text = lifeIndex.carWashing[0].desc
                weatherLayout.visibility = View.VISIBLE
            }
        }
    }

}
