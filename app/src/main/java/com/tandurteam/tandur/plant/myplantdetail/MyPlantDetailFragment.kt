package com.tandurteam.tandur.plant.myplantdetail

import android.app.Dialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.appbar.AppBarLayout
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.MonthlyLocationConditionAdapter
import com.tandurteam.tandur.core.constant.MapsConstant
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.DialogHarvestingBinding
import com.tandurteam.tandur.databinding.FragmentMyPlantDetailBinding
import com.tandurteam.tandur.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.math.abs

class MyPlantDetailFragment : Fragment() {

    private val viewModel: MyPlantDetailViewModel by viewModel()
    private val navArg: MyPlantDetailFragmentArgs by navArgs()
    private var _binding: FragmentMyPlantDetailBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MonthlyLocationConditionAdapter
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var isHarvested: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMyPlantDetailBinding.inflate(inflater, container, false)
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
                    binding.tvStatusDetail.visibility = View.GONE
                    binding.tvAppbarProbability.visibility = View.VISIBLE
                    binding.tvAppbarProbability.animate().alpha(1F).duration = 300

                    if (isHarvested == 1) {
                        binding.ivSatisfactionFace.visibility = View.GONE
                    }
                } else if (verticalOffset == 0) {
                    // when expanded
                    binding.tvStatusDetail.visibility = View.VISIBLE
                    binding.tvStatusDetail.animate().alpha(1F).duration = 300
                    binding.tvAppbarProbability.visibility = View.GONE

                    if (isHarvested == 1) {
                        binding.ivSatisfactionFace.visibility = View.VISIBLE
                        binding.ivSatisfactionFace.animate().alpha(1F).duration = 300
                    }
                } else {
                    // in between
                    binding.tvStatusDetail.visibility = View.VISIBLE
                    binding.tvStatusDetail.animate().alpha(percentage).duration = 300
                    binding.tvAppbarProbability.visibility = View.VISIBLE
                    binding.tvAppbarProbability.animate().alpha(percentage).duration = 300

                    if (isHarvested == 1) {
                        binding.ivSatisfactionFace.visibility = View.VISIBLE
                        binding.ivSatisfactionFace.animate().alpha(percentage).duration = 300
                    }
                }
                Log.d(TAG, "onViewCreated: $percentage")
            }
        )

        // hide bottom nav
        (requireActivity() as DashboardActivity).setBottomNavVisibility(false)

        // init adapter
        adapter = MonthlyLocationConditionAdapter()

        // set plant name by nav args
        binding.tvNamaTanamanDetail.text = navArg.plantName

        // on harvest clicked
        binding.btnHarvest.setOnClickListener {
            showHarvestDialog()
        }

        binding.swipeRefresh.setOnRefreshListener {
            observeLiveData(isHarvesting = false)
        }

        binding.tvUserLocation.setOnClickListener {
            Intent(requireActivity(), MapsActivity::class.java).apply {
                putExtra(MapsConstant.IS_READ_ONLY, true)
                putExtra(MapsConstant.LOCATION_DATA, LatLng(latitude, longitude))
                startActivity(this)
            }
        }

        // observe
        observeLiveData(isHarvesting = false)

        // on back pressed
        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }
    }

    private fun setLoadingState(isLoading: Boolean) {
        with(binding) {
            swipeRefresh.isRefreshing = isLoading

            val visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
            tvStatusDetail.visibility = visibility
            tvStatusPeringatan.visibility = visibility
            rvStatus.visibility = visibility
            icDate.visibility = visibility
            tvDate.visibility = visibility
            tvUserDate.visibility = visibility
            icTime.visibility = visibility
            tvDurasi.visibility = visibility
            tvUserDurasi.visibility = visibility
            icLocation.visibility = visibility
            tvLocation.visibility = visibility
            tvUserLocation.visibility = visibility
            vLine4.visibility = visibility
            vLine5.visibility = visibility
            vLine6.visibility = visibility

        }
    }

    private fun observeLiveData(satisfactionRate: Int = -1, isHarvesting: Boolean) {
        val viewModelFunction = if (isHarvesting) {
            viewModel.harvestPlant(navArg.plantId, satisfactionRate)
        } else {
            viewModel.getMyPlantDetail(navArg.plantName, navArg.plantId)
        }

        viewModelFunction.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result) {
                    is ApiResponse.Loading -> {
                        setLoadingState(true)
                    }

                    is ApiResponse.Success -> {
                        with(binding) {
                            val resultData = result.data.data
                            setLoadingState(false)

                            // set view
                            tvStatusDetail.text = resultData.probability.toInt().toString()
                            tvAppbarProbability.text = resultData.probability.toInt().toString()
                            tvUserLocation.text = requireContext().getString(
                                R.string.location_info,
                                resultData.zoneLocal,
                                resultData.zoneCity
                            )

                            try {
                                Glide.with(requireContext())
                                    .asBitmap()
                                    .load(resultData.imageUrl)
                                    .into(ivTanamanDetail)
                            } catch (e: Exception) {
                                Log.d(TAG, "observeLiveData: ${e.message}")
                            }

                            // set date
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                                val newPattern = DateTimeFormatter.ofPattern("dd MMMM yyyy")
                                val startDate = LocalDate.parse(
                                    resultData.plantStartDate,
                                    pattern
                                ).format(newPattern)
                                val harvestDate = LocalDate.parse(
                                    resultData.plantHarvestDate,
                                    pattern
                                ).format(newPattern)
                                tvUserDate.text = startDate
                                tvUserDurasi.text = harvestDate
                            } else {
                                tvUserDate.text = resultData.plantStartDate
                                tvUserDurasi.text = resultData.plantHarvestDate
                            }

                            // set harvested
                            isHarvested = resultData.isHarvested

                            // set probability background text color
                            if (
                                resultData.probability.toInt() <= 50
                            ) {
                                tvStatusDetail.setBackgroundResource(R.drawable.bg_circle_red)
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
                                tvStatusDetail.setBackgroundResource(R.drawable.bg_circle_green)

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

                            // assign latitude and longitude global variable
                            latitude = resultData.latitude
                            longitude = resultData.longitude

                            // check is harvested
                            if (resultData.isHarvested == 1) {
                                btnHarvest.visibility = View.GONE
                                ivSatisfactionFace.visibility = View.VISIBLE

                                // check satisfaction rate
                                if (resultData.satisfactionRate == 0) {
                                    ivSatisfactionFace.setImageResource(R.drawable.ic_sad)
                                    ivSatisfactionFace.imageTintList = ColorStateList.valueOf(
                                        ContextCompat.getColor(requireContext(), R.color.red_accent)
                                    )
                                } else if (resultData.satisfactionRate in (1..99)) {
                                    ivSatisfactionFace.setImageResource(R.drawable.ic_neutral)
                                    ivSatisfactionFace.imageTintList = ColorStateList.valueOf(
                                        ContextCompat.getColor(requireContext(), R.color.red_accent)
                                    )
                                }
                            }

                            // set adapter
                            Log.d(TAG, "getMyPlantPlantDetail: ${resultData.monthlyData}")
                            Log.d(TAG, "getMyPlantPlantDetail: ${resultData.monthlyData.size}")
                            adapter.setData(resultData.monthlyData, resultData.fixedData)
                            rvStatus.adapter = adapter
                        }
                    }
                    is ApiResponse.Error -> {
                        setLoadingState(false)
                        Toast.makeText(
                            requireContext(),
                            result.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                        Log.d(TAG, "observeLiveData: ${result.errorMessage}")
                    }
                    else -> {
                        setLoadingState(false)
                        Toast.makeText(
                            requireContext(),
                            "Terdapat kesalahan saat menghubungkan ke server.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun showHarvestDialog() {
        val dialog = Dialog(requireContext())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setCancelable(true)
            val dialogBinding = DialogHarvestingBinding.inflate(
                LayoutInflater.from(requireContext())
            )
            setContentView(dialogBinding.root)

            // init satisfaction rate
            var satisfactionRate = -1

            // set view
            with(dialogBinding) {
                ivSadFace.setOnClickListener {
                    satisfactionRate = 0
                    setEmotionColor(ivSadFace, ivHappyFace, ivNeutralFace)
                    setTextEmotionColor(tvSad, tvHappy, tvNeutral)
                }

                ivNeutralFace.setOnClickListener {
                    satisfactionRate = 50
                    setEmotionColor(ivNeutralFace, ivSadFace, ivHappyFace)
                    setTextEmotionColor(tvNeutral, tvSad, tvHappy)
                }

                ivHappyFace.setOnClickListener {
                    satisfactionRate = 100
                    setEmotionColor(ivHappyFace, ivSadFace, ivNeutralFace)
                    setTextEmotionColor(tvHappy, tvSad, tvNeutral)
                }

                tvBack.setOnClickListener { dismiss() }

                btnConfirmHarvest.setOnClickListener {
                    if (satisfactionRate >= 0) {
                        dismiss()
                        observeLiveData(satisfactionRate, true)
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Pilih tingkat kepuasan terlebih dahulu.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            show()
        }
    }

    private fun setEmotionColor(
        clickedImage: ImageView,
        vararg otherImages: ImageView
    ) {
        clickedImage.imageTintList = ColorStateList.valueOf(
            ContextCompat.getColor(requireContext(), R.color.green_accent)
        )

        otherImages.forEach { otherImage ->
            otherImage.imageTintList = ColorStateList.valueOf(
                ContextCompat.getColor(requireContext(), R.color.dark_accent)
            )
        }
    }

    private fun setTextEmotionColor(
        clickedText: TextView,
        vararg otherTexts: TextView
    ) {
        clickedText.setTextColor(ContextCompat.getColor(requireContext(), R.color.green_accent))

        otherTexts.forEach { otherText ->
            otherText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_accent))
        }
    }

    companion object {
        private val TAG = MyPlantDetailFragment::class.java.simpleName
    }
}