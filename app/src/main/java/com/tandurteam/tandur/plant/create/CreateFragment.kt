package com.tandurteam.tandur.plant.create

import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.model.LatLng
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.adapter.MonthlyLocationConditionAdapter
import com.tandurteam.tandur.core.constant.MapsConstant
import com.tandurteam.tandur.core.model.network.ApiResponse
import com.tandurteam.tandur.core.model.network.plantdetail.response.MonthlyData
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.FragmentCreateBinding
import com.tandurteam.tandur.maps.MapsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class CreateFragment : Fragment() {

    private val viewModel: CreateViewModel by viewModel()
    private val args: CreateFragmentArgs by navArgs()
    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private lateinit var calendar: Calendar
    private lateinit var adapter: MonthlyLocationConditionAdapter
    private var monthlyData = mutableListOf<MonthlyData>()
    private var probability: Double = 0.0
    private var startDate: String? = null
    private var harvestDate: String? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentCreateBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // get user location
        getUserLocation()

        // init adapter
        adapter = MonthlyLocationConditionAdapter()

        // get plant detail
        getPlantDetail()

        calendar = Calendar.getInstance()
        val harvestTime = 5 // TODO: Waiting fixed data
        binding.etPlantDate.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                R.style.DialogTheme,
                { _, year, month, day ->
                    // set start date time
                    val choosenDate = listOf(year, month, day)
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, day)
                    setStartDate()

                    // set harvest date TODO: Waiting fixed data
                    calendar.add(Calendar.MONTH, harvestTime)
                    setHarvestDate()

                    // set back calendar
                    calendar.set(Calendar.YEAR, choosenDate[0])
                    calendar.set(Calendar.MONTH, choosenDate[1])
                    calendar.set(Calendar.DAY_OF_MONTH, choosenDate[2])
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            // Set min date
            val now = System.currentTimeMillis() - 1000
            datePickerDialog.datePicker.minDate = now

            // set max date
            val nowCalendar = Calendar.getInstance()
            nowCalendar.set(
                Calendar.YEAR,
                SimpleDateFormat("yyyy", Locale.getDefault()).format(
                    System.currentTimeMillis()
                ).toInt()
            )
            nowCalendar.set(
                Calendar.MONTH,
                SimpleDateFormat("MM", Locale.getDefault()).format(
                    System.currentTimeMillis()
                ).toInt()
            )
            nowCalendar.set(
                Calendar.DAY_OF_MONTH,
                SimpleDateFormat("dd", Locale.getDefault()).format(
                    System.currentTimeMillis()
                ).toInt()
            )
            nowCalendar.add(Calendar.MONTH, 5 - harvestTime) // TODO: Waiting 5 - fixed data
            datePickerDialog.datePicker.maxDate = nowCalendar.time.time

            // show date picker
            datePickerDialog.show()
        }

        binding.etPlantingLocation.setOnClickListener {
            Intent(requireActivity(), MapsActivity::class.java).apply {
                putExtra(MapsConstant.LOCATION_DATA, LatLng(latitude, longitude))
                startActivity(this)
            }
        }

        binding.tvPlantName.text = args.plantName

        binding.ivBack.setOnClickListener { requireActivity().onBackPressed() }

        binding.btnTanam.setOnClickListener {
            if (validateCreatePlant()) createPlant()
        }
    }

    private fun validateCreatePlant(): Boolean {
        if (monthlyData.isEmpty()) {
            Toast.makeText(requireContext(), "Sedang memuat data, coba lagi", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        if (binding.etPlantDate.text.toString().isEmpty()) {
            Toast.makeText(requireContext(), "Isi waktu tanam terlebih dahulu", Toast.LENGTH_SHORT)
                .show()
            return false
        }
        return true
    }

    private fun createPlant() {
        viewModel.createPlant(
            args.plantName,
            monthlyData,
            probability,
            startDate.toString(),
            harvestDate.toString()
        )
            .observe(viewLifecycleOwner) {
                it?.let { plantResponse ->
                    when (plantResponse) {
                        is ApiResponse.Loading -> {
                            // set visibility
                            binding.progressLoading.visibility = View.VISIBLE
                            binding.btnTanam.visibility = View.GONE
                        }
                        is ApiResponse.Success -> {
                            // set visibility
                            binding.progressLoading.visibility = View.GONE
                            binding.btnTanam.visibility = View.VISIBLE

                            Log.d(TAG, "createPlant: ${plantResponse.data.data}")

                            // back to dashboard
                            Intent(requireActivity(), DashboardActivity::class.java).apply {
                                Toast.makeText(
                                    requireContext(),
                                    "${args.plantName} berhasil ditanam",
                                    Toast.LENGTH_SHORT
                                ).show()
                                startActivity(this)
                                requireActivity().finish()
                            }
                        }
                        else -> {
                            // set visibility
                            binding.progressLoading.visibility = View.GONE
                            binding.btnTanam.visibility = View.VISIBLE

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

    private fun getUserLocation() {
        viewModel.getUserLocation().observe(viewLifecycleOwner) {
            it?.let { userLocation ->
                binding.etPlantingLocation.setText(
                    requireContext().getString(
                        R.string.location_info,
                        userLocation.subZone,
                        userLocation.city
                    )
                )

                // assign latitude and longitude global variable
                if (userLocation.latitude != null && userLocation.longitude != null) {
                    latitude = userLocation.latitude
                    longitude = userLocation.longitude
                }
            }
        }
    }

    private fun getPlantDetail() {
        viewModel.getPlantDetail(args.plantName, startDate, harvestDate)
            .observe(viewLifecycleOwner) {
                it?.let { result ->
                    when (result) {
                        is ApiResponse.Loading -> {
                            binding.progressLoadingStatus.visibility = View.VISIBLE
                            binding.rvMonthlyCondition.visibility = View.GONE
                        }

                        is ApiResponse.Success -> {
                            with(binding) {
                                val resultData = result.data.data
                                progressLoadingStatus.visibility = View.GONE
                                rvMonthlyCondition.visibility = View.VISIBLE

                                // get monthly data
                                resultData?.monthlyData?.let { data ->
                                    monthlyData.addAll(data)
                                }

                                // get probability
                                resultData?.probability?.let { plantProbability ->
                                    // set probability text value
                                    tvPlantProbability.text = requireContext().getString(
                                        R.string.plant_probability,
                                        plantProbability.toInt().toString()
                                    )

                                    // set probability text color
                                    tvPlantProbability.setTextColor(
                                        if (plantProbability.toInt() <= 50) {
                                            ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.red_accent
                                                )
                                            )
                                        } else {
                                            ColorStateList.valueOf(
                                                ContextCompat.getColor(
                                                    requireContext(),
                                                    R.color.green_accent
                                                )
                                            )
                                        }
                                    )

                                    // set probability request body
                                    probability = plantProbability
                                }

                                // set adapter data
                                Log.d(TAG, "getPlantDetail: ${resultData?.monthlyData}")
                                adapter.setData(resultData?.monthlyData, resultData!!.fixedData)
                                rvMonthlyCondition.adapter = adapter
                            }
                        }

                        else -> {
                            binding.progressLoadingStatus.visibility = View.GONE
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setStartDate() {
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar.time)
        binding.etPlantDate.setText(date)

        // set start date request body
        val requestDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        startDate = requestDate
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun setHarvestDate() {
        val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(calendar.time)
        binding.etEstimatedHarvestTime.setText(date)

        // set harvest date request body
        val requestDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        harvestDate = requestDate

        // call plant detail again
        getPlantDetail()
    }

    override fun onResume() {
        super.onResume()
        getUserLocation()
        getPlantDetail()
    }

    companion object {
        private val TAG = CreateFragment::class.java.simpleName
    }
}