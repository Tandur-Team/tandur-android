package com.tandurteam.tandur.dashboard.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.maps.model.LatLng
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.NearbyPlantAdapter
import com.tandurteam.tandur.core.constant.MapsConstant
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.DialogLocationNotFoundBinding
import com.tandurteam.tandur.databinding.FragmentHomeBinding
import com.tandurteam.tandur.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var nearbyPlantAdapter: NearbyPlantAdapter
    private var dialogLocation: Dialog? = null
    private var isDialogOpened: Boolean = false
    private var query = ""
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get user full name
        getUserFullName()

        // get user location
        getUserLocation()

        // on refresh swiped
        binding.swipeRefresh.setOnRefreshListener {
            binding.etSearch.setText("")
            showDailyWeather()
            showNearbyPlant()
        }

        binding.tvLocation.setOnClickListener {
            Intent(requireActivity(), MapsActivity::class.java).apply {
                putExtra(MapsConstant.LOCATION_DATA, LatLng(latitude, longitude))
                requireActivity().startActivity(this)
            }
        }

        // init adapter for nearby plant
        nearbyPlantAdapter = NearbyPlantAdapter()
        nearbyPlantAdapter.onItemClick = { nearbyPlant ->
            navigateToDetail(nearbyPlant.plantName)
        }

        // show daily weather
        showDailyWeather()

        // search for neaby plant
        with(binding) {
            etSearch.addTextChangedListener {
                query = it.toString()
                // show nearby plant
                showNearbyPlant()
            }
        }

        // set temperature views
        with(binding.itemTemp) {
            ivCondition.setImageResource(R.drawable.ic_temperature)
            tvCoditionTitle.text = requireContext().getString(R.string.suhu_udara)
        }

        // set humidity views
        with(binding.itemHumidity) {
            ivCondition.setImageResource(R.drawable.ic_humidity)
            tvCoditionTitle.text = requireContext().getString(R.string.humiditas)
            tvCoditionTitle.maxLines = 1
        }
    }

    private fun showDailyWeather() {
        viewModel.getDailyWeather().observe(viewLifecycleOwner) {
            it?.let { dailyWeather ->
                when (dailyWeather) {
                    is ApiResponse.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                        setLoadingState(true)
                    }

                    is ApiResponse.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                        setLoadingState(false)

                        // set view
                        val resultData = dailyWeather.data.data
                        with(binding) {
                            itemRainfall.tvCondition.text = resultData?.rain.toString()
                            itemTemp.tvCondition.text = resultData?.temperature.toString()
                            itemHumidity.tvCondition.text = resultData?.humidity.toString()
                        }
                    }
                    else -> {
                        setLoadingState(false)
                        Log.d(TAG, "$dailyWeather")
                    }
                }
            }
        }
    }

    private fun getUserFullName() {
        viewModel.getUserFullName().observe(viewLifecycleOwner) {
            it?.let { fullName ->
                binding.tvWelcome.text = requireContext().getString(
                    R.string.welcome,
                    fullName
                )
            }
        }
    }

    private fun getUserLocation() {
        viewModel.getUserLocation().observe(viewLifecycleOwner) {
            it?.let { userLocation ->
                if (userLocation.latitude == 0.0 || userLocation.longitude == 0.0) {
                    // set location info text
                    binding.tvLocation.text = requireContext().getString(
                        R.string.click_to_get_your_location
                    )

                    Log.d(TAG, "getUserLocation: Still Null")

                    // show set location info dialog
                    if (!isDialogOpened) {
                        showSetLocationDialog()
                    }
                    Log.d(TAG, "getUserLocation: $dialogLocation")
                } else {
                    // assign latitude and longitude global variable
                    if (userLocation.latitude != null && userLocation.longitude != null) {
                        latitude = userLocation.latitude
                        longitude = userLocation.longitude
                    }

                    // set location info text
                    binding.tvLocation.text = requireContext().getString(
                        R.string.location_info,
                        userLocation.subZone,
                        userLocation.city
                    )

                    Log.d(TAG, "getUserLocation: $dialogLocation")
                    Log.d(TAG, "getUserLocation: Not Null")
                }
            }
        }
    }

    private fun showSetLocationDialog() {
        isDialogOpened = true
        dialogLocation = Dialog(requireContext())
        dialogLocation?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(false)
            val dialogBinding = DialogLocationNotFoundBinding.inflate(
                LayoutInflater.from(requireContext())
            )
            setContentView(dialogBinding.root)

            dialogBinding.btnSetMyLocation.setOnClickListener {
                Intent(requireActivity(), MapsActivity::class.java).apply {
                    startActivity(this)
                }
                isDialogOpened = false
                this.dismiss()
            }

            this.show()
        }
    }

    override fun onResume() {
        super.onResume()

        // show bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(true)

        // get user location info
        getUserLocation()

        // show daily weather
        showDailyWeather()

        // show nearby plant data again
        showNearbyPlant()
    }

    private fun navigateToDetail(plantName: String) {
        val action = HomeFragmentDirections.navigateToDetailFragment(plantName)
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun setLoadingState(isLoading: Boolean, isSearching: Boolean = false) {
        with(binding) {
            swipeRefresh.isRefreshing = isLoading

            val visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            val progressVisibility = if (!isLoading) View.INVISIBLE else View.VISIBLE
            if (!isSearching) {
                progressBarHorizontal.visibility = progressVisibility
                itemRainfall.cardCondition.visibility = visibility
                itemTemp.cardCondition.visibility = visibility
                itemHumidity.cardCondition.visibility = visibility
            }
            progressBarVertical.visibility = progressVisibility
            rvSaran.visibility = visibility
            tvError.visibility = View.GONE
        }
    }

    private fun showNearbyPlant() {
        viewModel.getNearbyPlant(query).observe(viewLifecycleOwner) {
            it?.let { nearbyPlant ->
                when (nearbyPlant) {
                    is ApiResponse.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                        setLoadingState(true, isSearching = true)
                    }

                    is ApiResponse.Success -> {
                        setLoadingState(false, isSearching = true)
                        binding.swipeRefresh.isRefreshing = false
                        binding.tvError.visibility = View.GONE

                        nearbyPlantAdapter.setData(nearbyPlant.data.data)

                        binding.rvSaran.apply {
                            setHasFixedSize(true)
                            adapter = this@HomeFragment.nearbyPlantAdapter
                        }

                    }

                    is ApiResponse.Error -> {
                        setLoadingState(false, isSearching = true)
                        binding.swipeRefresh.isRefreshing = false

                        // set visibility
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvSaran.visibility = View.GONE

                        binding.tvError.text =
                            requireContext().getString(R.string.error_connecting_to_api)
                    }

                    is ApiResponse.Empty -> {
                        setLoadingState(false, isSearching = true)
                        binding.swipeRefresh.isRefreshing = false

                        // set visibility
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvSaran.visibility = View.GONE

                        binding.tvError.text =
                            requireContext().getString(R.string.search_not_found)
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
}



