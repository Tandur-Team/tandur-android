package com.tandurteam.tandur.dashboard.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tandurteam.tandur.core.adapter.FixedPlantAdapter
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.databinding.FragmentHomeBinding
import com.tandurteam.tandur.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModel()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: FixedPlantAdapter

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

        binding.tvLocation.setOnClickListener {
            Intent(requireActivity(), MapsActivity::class.java).apply {
                requireActivity().startActivity(this)
            }
        }

        // init adapter
        adapter = FixedPlantAdapter()
        adapter.onItemClick = { fixedPlant ->
            // TODO: On item clicked should be go to detail
        }

        // observe live data
        observeLiveData()
    }

    private fun observeLiveData() {
        viewModel.getAllFixedPlant().observe(viewLifecycleOwner) {
            it?.let { fixedPlant ->
                when (fixedPlant) {
                    is ApiResponse.Loading -> {
                        Log.d(TAG, "Loading")
                    }

                    is ApiResponse.Success -> {
                        adapter.setData(fixedPlant.data.data)
                        binding.rvApaYangMerekaTanam.apply {
                            setHasFixedSize(true)
                            adapter = this@HomeFragment.adapter
                        }
                    }

                    is ApiResponse.Error -> {
                        Toast.makeText(
                            requireContext(),
                            "Gagal Memuat Tanaman",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ApiResponse.Empty -> {
                        Log.d(TAG, "Empty")
                    }
                }
            }
        }
    }

    companion object {
        private val TAG = HomeFragment::class.java.simpleName
    }
}



