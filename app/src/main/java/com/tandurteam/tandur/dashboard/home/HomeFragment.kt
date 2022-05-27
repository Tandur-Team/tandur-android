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
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.FixedPlantAdapter
import com.tandurteam.tandur.core.adapter.NearbyPlantAdapter
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
    private lateinit var fixPlantAdapter: FixedPlantAdapter
    private lateinit var nearbyPlantAdapter: NearbyPlantAdapter
    private var dialogLocation: Dialog? = null
    private var isDialogOpened: Boolean = false

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

        // get user location
        getUserLocation()

        binding.tvLocation.setOnClickListener {
            Intent(requireActivity(), MapsActivity::class.java).apply {
                requireActivity().startActivity(this)
            }
        }

        // init adapter for fixed plant
        fixPlantAdapter = FixedPlantAdapter()
        fixPlantAdapter.onItemClick = { fixedPlant ->
            navigateToDetail(fixedPlant.plantName)
        }

        // init adapter for nearby plant
        nearbyPlantAdapter = NearbyPlantAdapter()
        nearbyPlantAdapter.onItemClick = { nearbyPlant ->
            navigateToDetail(nearbyPlant.plantName)
        }

        // observe live data
        observeLiveData()
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
    }

    private fun navigateToDetail(plantName: String) {
        val action = HomeFragmentDirections.navigateToDetailFragment(plantName)
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun observeLiveData() {
        viewModel.getAllFixedPlant().observe(viewLifecycleOwner) {
            it?.let { fixedPlant ->
                when (fixedPlant) {
                    is ApiResponse.Success -> {
                        fixPlantAdapter.setData(fixedPlant.data.data)
                        binding.rvApaYangMerekaTanam.apply {
                            setHasFixedSize(true)
                            adapter = this@HomeFragment.fixPlantAdapter
                        }
                    }
                    else -> {
                        Log.d(TAG, "$fixedPlant")
                    }
                }
            }
        }

        viewModel.getNearbyPlant().observe(viewLifecycleOwner) {
            it?.let { nearbyPlant ->
                when (nearbyPlant) {
                    is ApiResponse.Success -> {
                        nearbyPlantAdapter.setData(nearbyPlant.data.data)
                        binding.rvSaran.apply {
                            setHasFixedSize(true)
                            adapter = this@HomeFragment.nearbyPlantAdapter
                        }

                    }
                    else -> {
                        Log.d(TAG, "$nearbyPlant")
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
}



