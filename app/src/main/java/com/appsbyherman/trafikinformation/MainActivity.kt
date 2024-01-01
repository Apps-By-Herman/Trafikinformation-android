package com.appsbyherman.trafikinformation

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.SyncStateContract
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceManager
import com.appsbyherman.trafikinformation.databinding.ActivityMainBinding
import com.appsbyherman.trafikinformation.models.Situation
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

class MainActivity : AppCompatActivity() {

    /*
    * TODO
    * - Add setting to set distance to locations
    * - If chosen area code, one can keep polling for new messages and show as push notifications
    *   that is pulled in a regular interval
    * - Implement a way to show all situations in a list
    * - Implement ads
    *   - Remove for f-droid version in build config
    *   - Implement in app purchase to buy non ads version
    * - Make it possible to use in background to retrieve push notifications when new situations occurs
    * - Is there a way of automatically start a location service when start driving?
    * */

    private val debugTag = "MainActivity"
    private lateinit var binding: ActivityMainBinding
    private var doubleBackToExitPressedOnce = false
    private val doubleBackToExitRunnable = Runnable { doubleBackToExitPressedOnce = false }
    private var locationListener: LocationListener? = null

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true -> getLocation()
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true -> getLocation()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Osmdroid stuff
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Setups
        setupMap()
        getLocation()

        binding.btnStart.setOnClickListener {
            actionOnService(Actions.START)
        }

        binding.btnStop.setOnClickListener {
            actionOnService(Actions.STOP)
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (doubleBackToExitPressedOnce) {
                    finish()
                }
                else {
                    doubleBackToExitPressedOnce = true
                    Toast.makeText(this@MainActivity,
                        getString(R.string.main_activity_double_back_press_to_exit_message),
                        Toast.LENGTH_SHORT).show()

                    Handler(Looper.myLooper()!!).postDelayed(doubleBackToExitRunnable, 2000)
                }
            }
        })
    }

    private fun getSituations(geoPoint: GeoPoint?, countyCode: Int? = null) {
        lifecycleScope.launch {
            val situations = withContext(Dispatchers.IO) { Requests.getSituations(geoPoint, countyCode = countyCode) }

            binding.pbLoading.visibility = View.GONE

            if (situations.isEmpty()) {
                Snackbar.make(binding.root, getString(R.string.fragment_home_rest_areas_error),
                    Snackbar.LENGTH_LONG)
                    .apply {
                        duration = 5000
                        setAction(getString(R.string.fragment_home_retry)) {
                            getSituations(geoPoint)
                        }
                        setBackgroundTint(ContextCompat.getColor(context, R.color.black))
                        setTextColor(ContextCompat.getColor(context, R.color.white))
                        setActionTextColor(ContextCompat.getColor(context, R.color.white))
                        show()
                    }

                return@launch
            }

            createMarkers(situations)
        }
    }

    private fun createMarkers(situations: List<Situation>) {
        val clusterMarkers = RadiusMarkerClusterer(this@MainActivity)
        binding.map.overlays.add(clusterMarkers)

        for (situation in situations) {
            for (deviation in situation.deviation) {
                val marker = Marker(binding.map).apply {
                    title = deviation.header
                    snippet = deviation.message
                    position = Helpers.getGeoPoint(deviation.geometry.point.wgs84)
                    icon =  ContextCompat.getDrawable(this@MainActivity,
                        // TODO: Fix this, get correct icon from api
                        when (deviation.iconId) {
                            "ferryDepartureOnSchedule" -> R.drawable.ic_launcher_foreground
                            "trafficMessage" -> R.drawable.ic_launcher_foreground
                            "roadwork" -> R.drawable.ic_launcher_foreground
                            "roadClosed" -> R.drawable.ic_launcher_foreground
                            else -> R.drawable.ic_launcher_foreground
                    })
                    setOnMarkerClickListener { _, _ ->
                        //TODO: Open dialog/popup thingy from bottom/new activity with more info about situation
                        return@setOnMarkerClickListener true
                    }
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                }

                clusterMarkers.add(marker)
            }
        }

        binding.map.invalidate()
    }

    private fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return

        Intent(this, EndlessService::class.java).also {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(it)
                return
            }

            startService(it)
        }
    }

    private fun setupMap() {
        binding.map.controller.apply {
            setZoom(6.0)
            setCenter(GeoPoint(62.500511, 15.283300))
        }

        binding.map.apply {
            setMultiTouchControls(true)
            zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)
        }
    }

    private fun addLocationOverlay() {
        val locationOverlay = MyLocationNewOverlay(binding.map).apply {
            enableMyLocation()
        }

        binding.map.overlays.add(locationOverlay)
    }

    private fun hasPermissions() : Boolean {
        return ContextCompat.checkSelfPermission(this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this@MainActivity,
                    Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {

        binding.pbLoading.visibility = View.VISIBLE

        if (!hasPermissions()) {
            locationPermissionRequest.launch(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION))

            getSituations(null)

            return
        }

        addLocationOverlay()

        val locationManager = getSystemService(LOCATION_SERVICE) as? LocationManager

        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                showUserLocation(location)
                getSituations(GeoPoint(location))
                locationListener?.let { locationManager?.removeUpdates(it) }
            }

            override fun onProviderEnabled(provider: String) {}

            override fun onProviderDisabled(provider: String) {}

            // This is done to prevent crash on older devices
            @Deprecated("Deprecated in Java")
            override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
        }

        val location = getLastBestLocation(locationManager)
        if  (location != null) {
            showUserLocation(location)
            getSituations(GeoPoint(location))
            return
        }

        val hasGps = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val hasNetwork = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

        if (hasGps == true) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                500,
                10000f,
                locationListener!!
            )
        }
        else if (hasNetwork == true) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                500,
                10000f,
                locationListener!!
            )
        }
        else {
            Toast.makeText(this@MainActivity,
                R.string.fragment_home_location_error,
                Toast.LENGTH_LONG).show()
        }
    }

    private fun showUserLocation(location: Location) {
        binding.map.controller.apply {
            animateTo(GeoPoint(location))

            setZoom(8.0)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastBestLocation(locationManager: LocationManager?): Location? {
        val locationGPS = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val locationNet = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        var gPSLocationTime: Long = 0
        if (locationGPS != null) {
            gPSLocationTime = locationGPS.time
        }

        var netLocationTime: Long = 0
        if (locationNet != null) {
            netLocationTime = locationNet.time
        }
        return if (0 < gPSLocationTime - netLocationTime) {
            locationGPS
        } else {
            locationNet
        }
    }
}