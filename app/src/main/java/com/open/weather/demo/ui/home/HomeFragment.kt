package com.open.weather.demo.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.open.weather.demo.R
import com.open.weather.demo.data.local.City
import com.open.weather.demo.databinding.FragmentHomeBinding
import com.open.weather.demo.utils.Constants.SHOWCASE_ID_MAP
import com.open.weather.demo.viewmodel.HomeViewModel
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView
import uk.co.deanwild.materialshowcaseview.shape.NoShape
import java.util.*

class HomeFragment : Fragment(), OnMapReadyCallback, TextWatcher {

    private var mapFragment: SupportMapFragment? = null
    private var googleMap: GoogleMap? = null
    private var homeViewModel: HomeViewModel? = null
    private var dataBinding: FragmentHomeBinding? = null
    private var adapter: CityAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        dataBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return dataBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (googleMap == null) {

            mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
            mapFragment?.getMapAsync(this@HomeFragment)
        }

        homeViewModel?.getBookmarkedCities()?.observe(viewLifecycleOwner) { cities ->

            when(cities.size){
                0 -> {

                    dataBinding?.edtSearch?.visibility = View.GONE
                    dataBinding?.rvCities?.visibility = View.GONE
                    googleMap?.clear()
                }
                else -> {

                    dataBinding?.edtSearch?.visibility = View.VISIBLE
                    addMarkerToMap(cities)
                    adapter = CityAdapter(cities) { city, isForDelete ->

                        when (isForDelete) {
                            true -> homeViewModel?.removeFromBookmark(city)
                            false -> moveToCity(city?.latitude, city?.longitude, true)
                        }
                    }
                    dataBinding?.rvCities?.adapter = adapter
                }
            }
        }

        dataBinding?.edtSearch?.addTextChangedListener(this)
    }

    override fun onMapReady(map: GoogleMap?) {

        val showCase = MaterialShowcaseView.Builder(requireActivity())
        showCase.setTarget(dataBinding?.root)
        showCase.setShape(NoShape())
        showCase.setDismissText("GOT IT")
        showCase.setContentText("By clicking on map you will able to see the weather of clicked place.")
        showCase.singleUse(SHOWCASE_ID_MAP)
        showCase.show()

        googleMap = map
        googleMap?.uiSettings?.isMapToolbarEnabled = false

        googleMap?.setOnMapClickListener { latLng ->

            googleMap?.addMarker(MarkerOptions().position(latLng))
            moveToCity(latLng.latitude.toString(), latLng.longitude.toString())
        }
    }

    private fun addMarkerToMap(cities: List<City>){

        googleMap?.clear()
        cities.forEach { city ->

            val latLng = LatLng(city.latitude?.toDouble() ?: 0.0, city.longitude?.toDouble() ?: 0.0)
            googleMap?.addMarker(MarkerOptions().position(latLng))
        }
    }

    private fun moveToCity(latitude: String?, longitude: String?, isBookmark: Boolean = false) {

        val action = HomeFragmentDirections.actionNavigationHomeToCityFragment(latitude, longitude, isBookmark)
        findNavController().navigate(action)
    }

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }

    override fun onTextChanged(query: CharSequence?, p1: Int, p2: Int, p3: Int) {

        adapter?.filter?.filter(query.toString())
    }

    override fun afterTextChanged(p0: Editable?) {

    }

    override fun onDestroy() {
        super.onDestroy()

        dataBinding?.edtSearch?.removeTextChangedListener(this)
    }
}