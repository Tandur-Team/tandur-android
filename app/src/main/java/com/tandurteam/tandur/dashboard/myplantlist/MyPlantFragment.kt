package com.tandurteam.tandur.dashboard.myplantlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tandurteam.tandur.core.adapter.MyPlantListAdapter
import com.tandurteam.tandur.core.model.network.ApiResponse
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
            // TODO: On item clicked should be go to detail
        }

        // on swipe refresh
        binding.swipeRefresh.setOnRefreshListener { observeLiveData() }

        // observe live data
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.getAllMyPlant().observe(viewLifecycleOwner) {
            it?.let { myPlantList ->
                when (myPlantList) {
                    is ApiResponse.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                    }

                    is ApiResponse.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                        adapter.setData(myPlantList.data.data)

                        // set adapter of rvListMovie
                        binding.rvTanamanku.apply {
                            setHasFixedSize(true)
                            adapter = this@MyPlantFragment.adapter
                        }
                    }

                    is ApiResponse.Error -> {
                        binding.swipeRefresh.isRefreshing = false
                        Toast.makeText(
                            requireContext(),
                            myPlantList.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ApiResponse.Empty -> {
                        binding.swipeRefresh.isRefreshing = false
                        Toast.makeText(requireContext(), "Your plant is empty", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }
}