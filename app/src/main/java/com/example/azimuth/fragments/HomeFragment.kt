package com.example.azimuth.fragments

import android.content.pm.PackageManager
import android.location.Location
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.azimuth.GridAdapter
import com.example.azimuth.GridViewModel
import com.example.azimuth.R
import com.example.azimuth.User
import com.example.azimuth.databinding.FragmentHomeBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var courseGrid: GridView
    private lateinit var courseList: List<GridViewModel>
    private lateinit var databaseRef: DatabaseReference

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mapFragment: SupportMapFragment
    private val permissionCode = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        getCurrentLocationUser()

//        fetchDataUser()
        updateUserProfile()
//        val timeStamp = SimpleDateFormat(
//            "EEEE MMMM dd, yyyy",
//            Locale.ENGLISH
//        ).format(Calendar.getInstance().time)
//        binding.curDate.text = timeStamp

        courseGrid = binding.gridv
        courseList = ArrayList()

        courseList = courseList + GridViewModel("Battery", "78%", R.drawable.battery_icon)
        courseList = courseList + GridViewModel("Control Box", "100%", R.drawable.energy)
        courseList = courseList + GridViewModel("Level of Rotary", "BASE", R.drawable.setting)

        val courseAdapter = context?.let { GridAdapter(courseList = courseList, it) }
        courseGrid.adapter = courseAdapter
        courseGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(context, courseList[position].name + " selected", Toast.LENGTH_SHORT)
                .show()
        }

        return binding.root
    }

    private fun getCurrentLocationUser() {
        if ((ActivityCompat.checkSelfPermission(
                requireActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) && (ActivityCompat.checkSelfPermission(
                requireActivity(),
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                permissionCode
            )
            return
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
//                Toast.makeText(
//                    context,
//                    currentLocation.latitude.toString() + "" + currentLocation.longitude.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()

                mapFragment = childFragmentManager.findFragmentById(R.id.currentLocationMaps) as SupportMapFragment
                mapFragment.getMapAsync(OnMapReadyCallback {
                    val latLng = LatLng(currentLocation.latitude, currentLocation.longitude)
                    val markerOptions = MarkerOptions().position(latLng).title("Your current location")
                    it.animateCamera(CameraUpdateFactory.newLatLng(latLng))
                    it.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                    it.addMarker(markerOptions)
                })
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

    //test fetch user data specific person
    private fun fetchDataUser() {

        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val person = dataSnapshot.getValue(User::class.java)
                        binding.userName.text = (person?.name ?: "Username")
                        binding.curDate.text = (person?.bio ?: "Bio")
                        Toast.makeText(
                            context,
                            "Hell $person?.name",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    "Failed to retrieve data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun updateUserProfile() {
//        val user = Firebase.auth.currentUser
//        val uid = user!!.uid
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
        databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children) {
                    val person = dataSnapshot.getValue(User::class.java)
                    person?.let {
                        binding.userName.text = "${it.name}"
                        binding.curDate.text = "${it.bio}"
                        Toast.makeText(context, "Hi ${it.name}!", Toast.LENGTH_SHORT).show()
                    }
                    break
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    context,
                    "Failed to retrieve data: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}