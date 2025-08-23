package com.example.azimuth.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.azimuth.LocationAdapter
import com.example.azimuth.LocationItem
import com.example.azimuth.R
import com.example.azimuth.api.APIService
import com.example.azimuth.api.BasicAuthClient
import com.example.azimuth.databinding.FragmentAutonomousBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import java.io.IOException
import java.lang.Double.parseDouble
import java.util.Locale

@Suppress("DEPRECATION")
class AutonomousFragment : Fragment(), OnMapReadyCallback, SnapshotReadyCallback {
    private var _binding: FragmentAutonomousBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mapFragment: SupportMapFragment
    private lateinit var currentLocation: Location
    private lateinit var googleMap: GoogleMap
    private val permissionCode = 101
    private lateinit var locationAdapter: LocationAdapter
    private val locationList = mutableListOf<LocationItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAutonomousBinding.inflate(inflater, container, false)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
//        getCurrentLocationUser()

        mapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            googleMap = map
            // Enable current location layer
            if (ContextCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                googleMap.isMyLocationEnabled = true
            }
        }

        locationAdapter = LocationAdapter(locationList)
        val recyclerView = binding.locationRecyclerview
        val layoutManager = LinearLayoutManager(context)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        recyclerView.layoutManager = layoutManager
        recyclerView.scrollToPosition(0)
        recyclerView.adapter = locationAdapter


        binding.getCurrentLocation.setOnClickListener {
            getCurrentLocationAndAddMarker()
        }
        binding.sendFab.setOnClickListener {
            val allItems = locationAdapter.getAllItems()
            Log.d("RecyclerViewDataLocation", "$allItems")
//            Toast.makeText(context, "$allItems", Toast.LENGTH_SHORT).show()
        }
        binding.backFab.setOnClickListener {

        }

        /*
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        getCurrentLocation()

        // Inflate the layout for this fragment
        binding.addFab.show()
        binding.sendFab.show()
        binding.backFab.show()
        binding.addFab.setOnClickListener {
            fetchLocation()
        }
        binding.sendFab.setOnClickListener {
//            val position: String = binding.position1.text.toString() + ", " +
//                    binding.position2.text.toString() + ", " +
//                    binding.position3.text.toString() + ", " +
//                    binding.position4.text.toString()
//            autonomous(position)
//            Toast.makeText(requireContext(), position, Toast.LENGTH_LONG).show()

//            val position = locationsList.joinToString(separator = ", ")

            var s = ""
            val locationsString = createLocationsString()
            for (item in locationsString){
                s += "$item, "
            }
            autonomous(s)
            Toast.makeText(requireContext(), s, Toast.LENGTH_LONG).show()

        }
        binding.backFab.setOnClickListener {
            autonomous("offline")
//            requireActivity().supportFragmentManager.beginTransaction().remove(this).commit()
//            locationsList = createListOfLocations()
//            Toast.makeText(requireContext(), locationsList.toString(), Toast.LENGTH_SHORT).show()
        }

         */

        return binding.root
    }

    private fun getCurrentLocationAndAddMarker() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    googleMap.addMarker(
                        MarkerOptions().position(currentLatLng)
                    )
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                    val latLng = LatLng(it.latitude, it.longitude)
                    val newLocationData = LocationItem(latLng)
                    locationList.add(newLocationData)
                    locationAdapter.notifyItemInserted(locationList.size - 1)
                }
            }
        } else {
            // Request location permissions
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addMarkerAndAddLocationItem() {

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = LatLng(it.latitude, it.longitude)
                    googleMap.clear() // Clear previous markers
                    googleMap.addMarker(
                        MarkerOptions().position(currentLatLng).title("Current Location")
                    )
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))

                    // Get address and add to RecyclerView
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    try {
                        val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                        val addressText =
                            addresses?.firstOrNull()?.getAddressLine(0) ?: "Unknown Address"
                        val latLng = LatLng(it.latitude, it.longitude)
//                        val newItem = LocationItem(addressText, latLng)
//                        locationList.add(newItem)
                        locationAdapter.notifyItemInserted(locationList.size - 1)
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
        } else {
            // Request permissions

        }
    }

    private fun getCurrentLocationUser() {
        if ((ActivityCompat.checkSelfPermission(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
                mapFragment =
                    childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync {
                    val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                    val markerOptions =
                        MarkerOptions().position(latLng).title("Your current location")
                    it.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    it.addMarker(markerOptions)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            permissionCode -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocationUser()
            }
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        TODO("Not yet implemented")
    }

    override fun onSnapshotReady(p0: Bitmap?) {
        // Handle the captured bitmap
    }
    private fun captureMapScreenshot() {
        googleMap.snapshot {

        }
    }

    /*
    private fun autonomous(directions: String) {
        val body: RequestBody = directions.toRequestBody("text/plain;charset=utf-8".toMediaType())
        val call = BasicAuthClient<APIService>().create(APIService::class.java)
        call.autonomous(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.i("API", "Autonomous Mode Online")
                } else {
                    Log.e("API", "Autonomous Mode Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API", t.message, t)
            }
        })
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                if (binding.position1.isFocused) {
                    binding.position1.setText("${it.latitude}, ${it.longitude}")
                }
                if (binding.position2.isFocused) {
                    binding.position2.setText("${it.latitude}, ${it.longitude}")
                }
                if (binding.position3.isFocused) {
                    binding.position3.setText("${it.latitude}, ${it.longitude}")
                }
                if (binding.position4.isFocused) {
                    binding.position4.setText("${it.latitude}, ${it.longitude}")
                }
            }
        }

    }

    private fun getCurrentLocation() {
        val task = fusedLocationProviderClient.lastLocation

        if (ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                currentLocation = it
                //Toast.makeText(requireContext(), currentLocation.latitude.toString() + " " + currentLocation.longitude.toString(), Toast.LENGTH_LONG).show()
                mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(OnMapReadyCallback {
                    googleMap = it.apply {
                        val location = LatLng(currentLocation.latitude, currentLocation.longitude)
                        mapType = GoogleMap.MAP_TYPE_SATELLITE
                        clear()
                        animateCamera(CameraUpdateFactory.newLatLng(location))
                        addMarker(MarkerOptions().position(location).title("Current Location"))
                        animateCamera(CameraUpdateFactory.newLatLngZoom(location, 18F))
                    }
                })
            }
        }

    }

    private fun createListOfLocations(): ArrayList<LatLng> {
        val list = arrayListOf<LatLng>()
        for (item in 0..binding.rootw.childCount) {
            val child = binding.rootw.getChildAt(item)
            if (child is TextInputLayout) {
                val editText = (child.getChildAt(0) as FrameLayout).getChildAt(0)
                if (editText is TextInputEditText) {
                    if (editText.text!!.isNotEmpty()){
                        list.add(parseToLatLng(editText.text.toString()))
                    } else {
                        continue
                    }
                }
            }
        }
        return list
    }

    private fun createLocationsString(): ArrayList<String> {
        val list = arrayListOf<String>()
        for (item in 0..binding.rootw.childCount) {
            val child = binding.rootw.getChildAt(item)
            if (child is TextInputLayout) {
                val editText = (child.getChildAt(0) as FrameLayout).getChildAt(0)
                if (editText is TextInputEditText) {
                    if (editText.text!!.isNotEmpty()){
                        list.add(editText.text.toString())
                    } else {
                        continue
                    }
                }
            }
        }
        return list
    }


    private fun parseToLatLng(convert: String): LatLng {
        val LatLong = convert.split(",")
        val latitude: Double = parseDouble(LatLong[0])
        val longitude: Double = parseDouble(LatLong[1])
        val locations = LatLng(latitude, longitude)

        return locations
    }


     */

}


