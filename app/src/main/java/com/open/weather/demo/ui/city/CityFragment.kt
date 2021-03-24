package com.open.weather.demo.ui.city

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.open.weather.demo.R
import com.open.weather.demo.databinding.FragmentCityBinding
import com.open.weather.demo.model.ForecastResponse
import com.open.weather.demo.network.Resource
import com.open.weather.demo.utils.Constants
import com.open.weather.demo.viewmodel.CityViewModel
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView

class CityFragment : Fragment() {

    private val args: CityFragmentArgs by navArgs()
    private var viewModel: CityViewModel? = null
    private var dataBinding: FragmentCityBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CityViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = FragmentCityBinding.inflate(layoutInflater,container, false)
        dataBinding?.viewModel = viewModel
        return dataBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel?.getWeather(args.latitude, args.longitude, Constants.METRIC)
        },1000)

        when(args.isBookmark){
            true -> dataBinding?.imgBookmark?.setImageResource(R.drawable.ic_bookmark)
            false -> dataBinding?.imgBookmark?.setImageResource(R.drawable.ic_unbookmark)
        }
        setObservers()
    }

    private fun setObservers() {

        viewModel?.bookMarkClick?.observe(this) {

            when(args.isBookmark){

                true -> {

                    dataBinding?.imgBookmark?.setImageResource(R.drawable.ic_unbookmark)
                    viewModel?.removeFromBookmark()
                }

                false -> {

                    dataBinding?.imgBookmark?.setImageResource(R.drawable.ic_bookmark)
                    viewModel?.bookmarkCity()
                }
            }
        }

        viewModel?.currentResponse?.observe(viewLifecycleOwner) { state ->

            when (state.status) {
                Resource.Status.SUCCESS -> {
                    dataBinding?.locationLoading?.visibility = View.GONE
                    dataBinding?.data = state.data
                    viewModel?.getForecast(args.latitude, args.longitude, Constants.METRIC)
                }
                Resource.Status.ERROR -> {
                    dataBinding?.locationLoading?.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                Resource.Status.LOADING -> {
                    dataBinding?.locationLoading?.visibility = View.VISIBLE
                }
            }
        }

        viewModel?.forecastDataResponse?.observe(viewLifecycleOwner) { state ->

            when (state.status) {
                Resource.Status.SUCCESS -> {

                    dataBinding?.locationLoading?.visibility = View.GONE
                    dataBinding?.rvForecast?.adapter = ForecastAdapter(forecastMapper(state.data?.list))

                    val showCase = MaterialShowcaseView.Builder(requireActivity())
                    showCase.setTarget(dataBinding?.imgBookmark)
                    showCase.setDismissText("GOT IT")
                    showCase.setContentText("By clicking on this button you can book mark your cities, list will be available on home screen")
                    showCase.singleUse(Constants.SHOWCASE_ID_BOOKMARK)
                    showCase.show()
                }
                Resource.Status.ERROR -> {
                    dataBinding?.locationLoading?.visibility = View.GONE
                    Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                }
                Resource.Status.LOADING -> {
                    dataBinding?.locationLoading?.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun forecastMapper(data: List<ForecastResponse.Forecast>?) : List<ForecastResponse.Forecast>? =
        data?.distinctBy { it.getDay() } // Eliminate same days
            ?.toList() // Return as list
}