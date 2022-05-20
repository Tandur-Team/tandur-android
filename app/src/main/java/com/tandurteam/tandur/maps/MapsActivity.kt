package com.tandurteam.tandur.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
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
import com.tandurteam.tandur.dashboard.DashboardActivity
import com.tandurteam.tandur.databinding.ActivityMapsBinding
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var intentData: String? = null
    private var locationFromIntent: LatLng? = null
    private var marker: Marker? = null
    private var selectedLocation: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // get intent data if exist
        intentData = intent.getStringExtra(INTENT_DATA)
        locationFromIntent = intent.getParcelableExtra(LOCATION_DATA)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // set other view
        with(binding) {
            // on back pressed
            btnBack.setOnClickListener { onBackPressed() }

            // action when intentData is exist
            intentData?.let {
                // set visibility
                btnMyLocation.visibility = View.VISIBLE
                btnAdd.visibility = View.VISIBLE

                // set my location button on click listener
                btnMyLocation.setOnClickListener { getMyLastLocation() }

                // set add button on click listener
                btnAdd.setOnClickListener {
                    selectedLocation?.let { latLng ->
                        Log.d(TAG, "onCreate: $latLng")
                        Intent(this@MapsActivity, DashboardActivity::class.java).apply {
                            putExtra(SET_LOCATION_DATA, latLng)
                            setResult(resultCode, this)
                        }
                        finish()
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

        getMyLocation()
        setMapStyle()

        // mark clicked map
        mMap.setOnMapClickListener {
            setMarkerLocation(it)
        }

        // check location intent
        locationFromIntent?.let { latLng ->
            moveAndAnimateCamera(latLng)
            intentData?.let { setMarkerLocation(latLng) }
        }
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
                    .title("Marked")
            )
        }

        // Get location info
        val address = geocoder.getFromLocation(
            location.latitude,
            location.longitude,
            1
        ).firstOrNull()?.getAddressLine(0)
        address?.let {
            binding.tvLocationInfo.text = it
        }

        selectedLocation = location
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

                    // set marker
                    if (locationFromIntent != null || intentData != null) {
                        setMarkerLocation(LatLng(location.latitude, location.longitude))
                    }
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
        const val INTENT_DATA = "Intent Data"
        const val LOCATION_DATA = "Location Intent Data"
        const val SET_LOCATION_DATA = "Set Location Data"
        const val resultCode = 202
    }
}