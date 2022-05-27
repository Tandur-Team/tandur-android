package com.tandurteam.tandur.plant.choose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tandurteam.tandur.core.adapter.NearbyPlantAdapter
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.FragmentChoosePlantBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoosePlantFragment : Fragment() {

    private val viewModel: ChoosePlantViewModel by viewModel()
    private var _binding: FragmentChoosePlantBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: NearbyPlantAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentChoosePlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // hide bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(false)

        // init adapter for nearby plant
        adapter = NearbyPlantAdapter()
        adapter.onItemClick = {
            navigateToCreate()
        }

        // observe live data
        observeLiveData()

        with(binding) {
            // on back pressed
            ivBack.setOnClickListener { requireActivity().onBackPressed() }
        }
    }

    private fun navigateToCreate() {
        val action = ChoosePlantFragmentDirections.navigateToCreateFragmentFromChooseFragment()
        Navigation.findNavController(binding.root).navigate(action)
    }


    private fun observeLiveData() {
        viewModel.getChoosePlant().observe(viewLifecycleOwner) {
            it?.let { choosePlant ->
                when (choosePlant) {
                    is ApiResponse.Success -> {
                        adapter.setData(choosePlant.data.data)
                        binding.rvRecommendedPlantList.apply {
                            setHasFixedSize(true)
                            adapter = this@ChoosePlantFragment.adapter
                        }
                    }
                    else -> {
                        Log.d(TAG, "$choosePlant")
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = ChoosePlantFragment::class.java.simpleName
    }
}