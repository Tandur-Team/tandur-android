package com.tandurteam.tandur.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.tandurteam.tandur.R
import com.tandurteam.tandur.core.constant.DataStoreConstant
import com.tandurteam.tandur.core.constant.MapsConstant
import com.tandurteam.tandur.databinding.ActivityMapsBinding
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val viewModel: MapsViewModel by viewModel()
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isReadOnly: Boolean = false
    private var locationFromIntent: LatLng? = null
    private var marker: Marker? = null
    private var selectedLocation: LatLng? = null
    private var city = ""
    private var subZone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // get intent data if exist
        isReadOnly = intent.getBooleanExtra(MapsConstant.IS_READ_ONLY, false)
        locationFromIntent = intent.getParcelableExtra(MapsConstant.LOCATION_DATA)
        Log.d(TAG, "onCreate: $locationFromIntent")

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // set other view
        with(binding) {
            if (isReadOnly) {
                btnAdd.visibility = View.GONE
            } else {
                // on back pressed
                btnBack.setOnClickListener { onBackPressed() }

                // set my location button on click listener
                btnMyLocation.setOnClickListener { getMyLastLocation() }

                // set add button on click listener
                btnAdd.setOnClickListener {
                    selectedLocation?.let { latLng ->
                        Log.d(TAG, "onCreate: $latLng")
                        lifecycleScope.launch {
                            viewModel.setUserLocation(city, DataStoreConstant.CITY)
                            viewModel.setUserLocation(subZone, DataStoreConstant.SUB_ZONE)
                            viewModel.setUserLatLng(latLng.latitude, DataStoreConstant.LATITUDE)
                            viewModel.setUserLatLng(latLng.longitude, DataStoreConstant.LONGITUDE)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = false
        mMap.uiSettings.isMyLocationButtonEnabled = false

        // get my location
        getMyLocation()

        // set map style
        setMapStyle()

        // mark clicked map
        if (!isReadOnly) {
            mMap.setOnMapClickListener {
                setMarkerLocation(it)
            }
        }

        // check location intent
        locationFromIntent?.let { latLng ->
            moveAndAnimateCamera(latLng)
            setMarkerLocation(latLng)
        } ?: run { getMyLastLocation() }
    }

    private fun setMarkerLocation(location: LatLng) {
        val geocoder = Geocoder(this, Locale.getDefault())

        marker = if (marker != null) {
            marker?.remove()
            mMap.addMarker(MarkerOptions().position(location))
        } else {
            mMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("${location.latitude}, ${location.longitude}")
            )
        }

        // Get location info
        try {
            val address = geocoder.getFromLocation(
                location.latitude,
                location.longitude,
                1
            ).firstOrNull()
            address?.let {
                city = it.subAdminArea
                subZone = it.locality
                binding.tvLocationInfo.text =
                    getString(R.string.location_info, it.locality, it.subAdminArea)
            }

            selectedLocation = location
        } catch (e: Exception) {
            Toast.makeText(this, "Coba pilih lokasi sekali lagi", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setMapStyle() {
        try {
            val success = mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style)
            )
            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (exception: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", exception)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLastLocation() {
        if (
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // animate camera
                    moveAndAnimateCamera(
                        LatLng(
                            location.latitude,
                            location.longitude
                        )
                    )
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun moveAndAnimateCamera(latLng: LatLng) {
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                latLng, 15f
            )
        )
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                // Precise location access granted.
                getMyLastLocation()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                // Only approximate location access granted.
                getMyLastLocation()
            }
            else -> {
                // No location access granted.
            }
        }
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private val TAG = MapsActivity::class.java.simpleName
    }
}