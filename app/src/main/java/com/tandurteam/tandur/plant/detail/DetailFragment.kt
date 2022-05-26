package com.tandurteam.tandur.plant.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.databinding.FragmentDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private val viewModel: PlantDetailViewModel by viewModel()
    private val navArgs: DetailFragmentArgs by navArgs()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPlantDetail()

        binding.btnTanamBaru.setOnClickListener {
            val action = DetailFragmentDirections.navigateToCreateFragmentFromDetailFragment()
            Navigation.findNavController(binding.root).navigate(action)
        }
    }

    private fun getPlantDetail() {
        viewModel.getPlantDetail(navArgs.plantName).observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result) {
                    is ApiResponse.Loading -> {
                        binding.swipeRefresh.isRefreshing = true
                    }

                    is ApiResponse.Success -> {
                        binding.swipeRefresh.isRefreshing = false
                        binding.tvProbability.text = result.data.data?.probability.toString()
                    }

                    else -> {
                        binding.swipeRefresh.isRefreshing = false
                        Toast.makeText(
                            requireContext(),
                            "Terdapat kesalahan saat menghubungkan ke server",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
}