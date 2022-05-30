package com.tandurteam.tandur.dashboard.myplantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.MyPlantListAdapter
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.FragmentMyPlantBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyPlantFragment : Fragment() {

    private val viewModel: MyPlantViewModel by viewModel()
    private var _binding: FragmentMyPlantBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MyPlantListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyPlantBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // init adapter
        adapter = MyPlantListAdapter()
        adapter.onItemClick = { myPlant ->
            if (myPlant.plantName != null && myPlant.id != null) {
                navigateToMyPlantDetail(myPlant.plantName, myPlant.id)
            }
        }

        // on swipe refresh
        binding.swipeRefresh.setOnRefreshListener { observeLiveData() }

        // on create plant clicked
        binding.btnTanamBaru.setOnClickListener {
            val action = MyPlantFragmentDirections.navigateToChooseFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }

        // observe live data
        observeLiveData()
    }

    private fun navigateToMyPlantDetail(plantName: String, plantId: String) {
        val action = MyPlantFragmentDirections.navigateToMyDetailPlantFragment(plantName, plantId)
        Navigation.findNavController(binding.root).navigate(action)
    }

    private fun observeLiveData() {
        viewModel.getAllMyPlant().observe(viewLifecycleOwner) {
            it?.let { myPlantList ->
                when (myPlantList) {
                    is ApiResponse.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                        binding.tvError.visibility = View.GONE
                    }

                    is ApiResponse.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                        binding.tvError.visibility = View.GONE

                        // set adapter of rvListMovie
                        adapter.setData(myPlantList.data.data)
                        binding.rvTanamanku.apply {
                            setHasFixedSize(true)
                            adapter = this@MyPlantFragment.adapter
                        }
                    }

                    is ApiResponse.Error -> {
                        binding.swipeRefresh.isRefreshing = false

                        // set visibility
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvTanamanku.visibility = View.GONE

                        // set message
                        binding.tvError.text =
                            requireContext().getString(R.string.error_connecting_to_api)
                    }

                    is ApiResponse.Empty -> {
                        binding.swipeRefresh.isRefreshing = false

                        // set visibility
                        binding.tvError.visibility = View.VISIBLE
                        binding.rvTanamanku.visibility = View.GONE

                        // set message
                        binding.tvError.text = requireContext().getString(R.string.empty_my_plant)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        // show bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(true)
    }
}