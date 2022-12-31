package com.myt.mytweather.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.myt.mytweather.databinding.FragmentPlaceBinding
import com.myt.mytweather.ui.weather.WeatherActivity

class PlaceFragment: Fragment(){

    private var binding: FragmentPlaceBinding? = null

    val viewModel by lazy {
        ViewModelProvider(this)[PlaceViewModel::class.java]
    }

    private lateinit var adapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (viewModel.isPlaceSaved()) {
            val place = viewModel.getSavedPlace()
            val intent = Intent(context, WeatherActivity::class.java).apply {
                putExtra("location_lng", place.location.lng)
                putExtra("location_lat", place.location.lat)
                putExtra("place_name", place.name)
            }
            startActivity(intent)
            activity?.finish()
            return
        }

        val layoutManager = LinearLayoutManager(activity)
        binding?.apply {
            recyclerView.layoutManager = layoutManager
            adapter = PlaceAdapter(this@PlaceFragment, viewModel.placeList)
            recyclerView.adapter = adapter
            searchPlaceEdit.addTextChangedListener {
                val content = it.toString()
                if(content.isNotEmpty()){
                    viewModel.searchPlaces(content)
                }else{
                    recyclerView.visibility = View.GONE
                    bgImageView.visibility = View.VISIBLE
                    viewModel.placeList.clear()
                    adapter.notifyDataSetChanged()
                }
            }
            viewModel.placeLiveData.observe(viewLifecycleOwner){
                val places = it.getOrNull()
                if(places != null){
                    recyclerView.visibility = View.VISIBLE
                    bgImageView.visibility = View.GONE
                    viewModel.placeList.clear()
                    viewModel.placeList.addAll(places)
                    adapter.notifyDataSetChanged()

                } else {
                    Toast.makeText(activity, "未能查询到任何地点", Toast.LENGTH_SHORT).show()
                    it.exceptionOrNull()?.printStackTrace()
                }
            }
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}