package com.tandurteam.tandur.plant.choose

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tandurteam.tandur.R
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
    private var query = ""

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
            navigateToCreate(it.plantName)
        }

        with(binding) {
            // observe live data
            etSearch.addTextChangedListener {
                query = it.toString()
                observeLiveData()
            }

            // on back pressed
            ivBack.setOnClickListener { requireActivity().onBackPressed() }
        }

        // observe live data
        observeLiveData()
    }

    private fun navigateToCreate(plantName: String) {
        val action = ChoosePlantFragmentDirections.navigateToCreateFragmentFromChooseFragment(
            plantName
        )
        Navigation.findNavController(binding.root).navigate(action)
    }


    private fun observeLiveData() {
        viewModel.getChoosePlant(query).observe(viewLifecycleOwner) {
            it?.let { choosePlant ->
                when (choosePlant) {
                    is ApiResponse.Loading -> {
                        setLoadingState(true)

                        Log.d(TAG, "observe: Loading")
                    }

                    is ApiResponse.Success -> {
                        binding.tvError.visibility = View.GONE

                        adapter.setData(choosePlant.data.data)
                        setLoadingState(false)

                        binding.rvRecommendedPlantList.apply {
                            setHasFixedSize(true)
                            adapter = this@ChoosePlantFragment.adapter
                        }

                        Log.d(TAG, "observe: Success")
                    }

                    is ApiResponse.Error -> {
                        // set visibility
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvRecommendedPlantList.visibility = View.GONE

                        binding.tvError.text =
                            requireContext().getString(R.string.error_connecting_to_api)

                        Log.d(TAG, "observe: Error")
                    }

                    is ApiResponse.Empty -> {
                        // set visibility
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvRecommendedPlantList.visibility = View.GONE

                        binding.tvError.text =
                            requireContext().getString(R.string.search_not_found)

                        Log.d(TAG, "observe: empty")
                    }
                }
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        with(binding) {
            val visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            rvRecommendedPlantList.visibility = visibility
        }

    }

    companion object {
        private val TAG = ChoosePlantFragment::class.java.simpleName
    }
}