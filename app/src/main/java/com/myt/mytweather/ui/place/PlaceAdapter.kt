package com.myt.mytweather.ui.place

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.myt.mytweather.databinding.PlaceItemBinding
import com.myt.mytweather.logic.model.Place

class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>): RecyclerView.Adapter<PlaceAdapter.ViewHolder>(){

    inner class ViewHolder(val viewBinding: PlaceItemBinding):RecyclerView.ViewHolder(viewBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding = PlaceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.viewBinding.run {
            placeName.text = place.name
            placeAddress.text = place.address
        }
    }

    override fun getItemCount() = placeList.size

}