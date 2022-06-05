package com.tandurteam.tandur.plant.detail

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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.AppBarLayout
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.MonthlyLocationConditionAdapter
import com.tandurteam.tandur.core.constant.MapsConstant
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.DialogGeospatialInfoBinding
import com.tandurteam.tandur.databinding.FragmentDetailBinding
import com.tandurteam.tandur.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class DetailFragment : Fragment() {

    private val viewModel: PlantDetailViewModel by viewModel()
    private val navArgs: DetailFragmentArgs by navArgs()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MonthlyLocationConditionAdapter
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

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
        (requireActivity() as DashboardActivity).setSupportActionBar(binding.toolbar)

        // set probability text visibility by app bar
        binding.appbar.addOnOffsetChangedListener(
            AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
                val percentage = (abs(verticalOffset) / appBarLayout.totalScrollRange).toFloat()
                if (abs(verticalOffset) == appBarLayout.totalScrollRange) {
                    // when collapsed
                    binding.tvProbability.visibility = View.GONE
                    binding.tvAppbarProbability.visibility = View.VISIBLE
                    binding.tvAppbarProbability.animate().alpha(1F).duration = 300
                } else if (verticalOffset == 0) {
                    // when expanded
                    binding.tvProbability.visibility = View.VISIBLE
                    binding.tvProbability.animate().alpha(1F).duration = 300
                    binding.tvAppbarProbability.visibility = View.GONE
                } else {
                    // in between
                    binding.tvProbability.visibility = View.VISIBLE
                    binding.tvProbability.animate().alpha(percentage).duration = 300
                    binding.tvAppbarProbability.visibility = View.VISIBLE
                    binding.tvAppbarProbability.animate().alpha(percentage).duration = 300
                }
                Log.d(TAG, "onViewCreated: $percentage")
            }
        )

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
                putExtra(MapsConstant.LOCATION_DATA, LatLng(latitude, longitude))
                startActivity(this)
            }
        }

        // on back pressed
        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }

        // on info clicked
        binding.ivInfo.setOnClickListener { showInfoDialog() }
    }

    private fun showInfoDialog() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            val dialogBinding = DialogGeospatialInfoBinding.inflate(
                LayoutInflater.from(requireContext())
            )
            setContentView(dialogBinding.root)

            // set view
            with(dialogBinding) {
                btnBack.setOnClickListener { dismiss() }
            }

            show()
        }
    }

    private fun getUserLocation() {
        viewModel.getUserLocation().observe(viewLifecycleOwner) {
            it?.let { userLocation ->
                binding.tvUserLocation.text = requireContext().getString(
                    R.string.location_info,
                    userLocation.subZone,
                    userLocation.city
                )

                // assign latitude and longitude global variable
                if (userLocation.latitude != null && userLocation.longitude != null) {
                    latitude = userLocation.latitude
                    longitude = userLocation.longitude
                }
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
            ivInfo.visibility = visibility
            btnTanamBaru.visibility = visibility
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
                            tvProbability.text = resultData?.probability?.toInt().toString()
                            resultData?.nearby?.let { nearbyCount ->
                                tvUserPerson.text = if (nearbyCount != 0) {
                                    requireContext().getString(
                                        R.string.nearby_farmer,
                                        nearbyCount.toString()
                                    )
                                } else {
                                    requireContext().getString(
                                        R.string.nearby_farmer_empty,
                                        resultData.plantName
                                    )
                                }
                            }
                            tvUserDurasi.text = requireContext().getString(
                                R.string.estimasi,
                                resultData?.duration.toString()
                            )

                            // set probability background text color
                            resultData?.probability?.let { probability ->
                                if (probability.toInt() <= 50) {
                                    tvProbability.setBackgroundResource(R.drawable.bg_circle_red)
                                    collapsingToolbar.setContentScrimColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.red_accent
                                        )
                                    )
                                    collapsingToolbar.setStatusBarScrimColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.red_accent
                                        )
                                    )
                                } else {
                                    tvProbability.setBackgroundResource(R.drawable.bg_circle_green)
                                    collapsingToolbar.setContentScrimColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.green_accent
                                        )
                                    )
                                    collapsingToolbar.setStatusBarScrimColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.green_accent
                                        )
                                    )
                                }
                            }

                            try {
                                Glide.with(requireContext())
                                    .asBitmap()
                                    .load(resultData?.imageUrl)
                                    .into(ivTanamanDetail)
                            } catch (e: Exception) {
                                Log.d(TAG, "getPlantDetail: ${e.message}")
                            }

                            // set adapter data
                            Log.d(TAG, "getPlantDetail: ${resultData?.monthlyData}")
                            adapter.setData(resultData?.monthlyData, resultData!!.fixedData)
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