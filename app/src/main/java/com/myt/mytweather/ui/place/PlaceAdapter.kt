package com.myt.mytweather.ui.place

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.myt.mytweather.databinding.PlaceItemBinding
import com.myt.mytweather.logic.model.Place
import com.myt.mytweather.ui.weather.WeatherActivity
import kotlinx.coroutines.NonDisposableHandle.parent

class PlaceAdapter(private val fragment: PlaceFragment, private val placeList: List<Place>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){

    inner class ViewHolder(val viewBinding: PlaceItemBinding):RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.apply {
            viewBinding.run {
                placeName.text = place.name
                placeAddress.text = place.address
            }
            itemView.setOnClickListener {
                val activity = fragment.activity
                if (activity is WeatherActivity) {
                    activity.activityWeatherBinding.drawerLayout.closeDrawers()
                    activity.viewModel.locationLng = place.location.lng
                    activity.viewModel.locationLat = place.location.lat
                    activity.viewModel.placeName = place.name
                    activity.refreshWeather()
                } else {
                    val intent = Intent(activity, WeatherActivity::class.java).
                    apply {
                        putExtra("location_lng", place.location.lng)
                        putExtra("location_lat", place.location.lat)
                        putExtra("place_name", place.name)
                    }
                    fragment.startActivity(intent)
                    activity?.finish()
                }
                fragment.viewModel.savePlace(place)
            }
        }
    }

    override fun getItemCount() = placeList.size

}