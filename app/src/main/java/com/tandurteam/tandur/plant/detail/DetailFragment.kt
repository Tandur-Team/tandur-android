package com.tandurteam.tandur.plant.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.MonthlyLocationConditionAdapter
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.FragmentDetailBinding
import com.tandurteam.tandur.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    private val viewModel: PlantDetailViewModel by viewModel()
    private val navArgs: DetailFragmentArgs by navArgs()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MonthlyLocationConditionAdapter

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

        // get user location
        getUserLocation()

        // hide bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(false)

        // init adapter
        adapter = MonthlyLocationConditionAdapter()

        // set plant name by nav args
        binding.tvNamaTanamanDetail.text = navArgs.plantName

        // get plant detail
        getPlantDetail()

        // on refresh swiped
        binding.swipeRefresh.setOnRefreshListener {
            getPlantDetail()
        }

        // on create plant clicked
        binding.btnTanamBaru.setOnClickListener {
            val plantName = binding.tvNamaTanamanDetail.text.toString()
            val action =
                DetailFragmentDirections.navigateToCreateFragmentFromDetailFragment(plantName)
            Navigation.findNavController(binding.root).navigate(action)
        }

        // on change location clicked
        binding.tvMessageLocation.setOnClickListener {
            Intent(requireActivity(), MapsActivity::class.java).apply {
                startActivity(this)
            }
        }

        // on back pressed
        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun getUserLocation() {
        viewModel.getUserLocation().observe(viewLifecycleOwner) {
            it?.let { userLocation ->
                binding.tvUserLocation.text = requireContext().getString(
                    R.string.location_info,
                    userLocation.subZone,
                    userLocation.city
                )
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        with(binding) {
            swipeRefresh.isRefreshing = isLoading

            val visibility = if (isLoading) View.GONE else View.VISIBLE
            tvProbability.visibility = visibility
            tvInformasiTerkait.visibility = visibility
            tvUserLocation.visibility = visibility
            tvUserPerson.visibility = visibility
            tvUserDurasi.visibility = visibility
            tvLocation.visibility = visibility
            tvPerson.visibility = visibility
            tvDurasi.visibility = visibility
            icLokasi.visibility = visibility
            icPerson.visibility = visibility
            icTime.visibility = visibility
            vLine1.visibility = visibility
            vLine2.visibility = visibility
            vLine3.visibility = visibility
            tvMessageLocation.visibility = visibility
            tvKondisiLingkungan.visibility = visibility
            rvKondisiLingkungan.visibility = visibility
        }
    }

    private fun getPlantDetail() {
        viewModel.getPlantDetail(navArgs.plantName).observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result) {
                    is ApiResponse.Loading -> {
                        setLoadingState(true)
                    }

                    is ApiResponse.Success -> {
                        with(binding) {
                            val resultData = result.data.data
                            setLoadingState(false)

                            // set views
                            tvProbability.text = resultData?.probability.toString()
                            Glide.with(requireContext())
                                .asBitmap()
                                .load(resultData?.imageUrl)
                                .into(ivTanamanDetail)

                            // set adapter data
                            Log.d(TAG, "getPlantDetail: ${resultData?.monthlyData}")
                            adapter.setData(resultData?.monthlyData)
                            rvKondisiLingkungan.adapter = adapter
                        }
                    }

                    else -> {
                        setLoadingState(false)
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

    override fun onResume() {
        super.onResume()
        getUserLocation()
        getPlantDetail()
    }

    companion object {
        private val TAG = DetailFragment::class.java.simpleName
    }
}