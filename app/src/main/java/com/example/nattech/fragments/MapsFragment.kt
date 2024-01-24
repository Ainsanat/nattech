package com.example.nattech.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.nattech.R
import com.example.nattech.api.APIService
import com.example.nattech.databinding.FragmentMapsBinding
import com.example.nattech.api.BasicAuthClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
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
import java.lang.Double.parseDouble


class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap
    lateinit var currentLocation: Location
    lateinit var locationsList: ArrayList<LatLng>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

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

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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

    //---unusable---
//    private fun addMarkerToMap() {
//        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(OnMapReadyCallback {
//            googleMap = it.apply {
//                val location = createListOfLocations()
//
//                for (i in 0..location.size) {
//                    addMarker(MarkerOptions().position(location[i]).title("Current Location"))
//                }
//            }
//        })
//    }

}


