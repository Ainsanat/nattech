package com.example.azimuth.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.azimuth.Device
import com.example.azimuth.R
import com.example.azimuth.databinding.FragmentDeviceBinding
import com.sjapps.library.customdialog.CustomViewDialog
import androidx.core.graphics.toColorInt
import com.example.azimuth.Autonomous
import com.example.azimuth.DeviceAdapter
import com.example.azimuth.Manual
import com.example.azimuth.Mode
import com.example.azimuth.Status
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DeviceFragment : Fragment() {
    private var _binding: FragmentDeviceBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference
    private lateinit var deviceList: ArrayList<Device>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)

        val user = Firebase.auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user)
        deviceList = arrayListOf()
        fetchDevices()

        binding.btnAddDevice.setOnClickListener {
            addDevice()
        }
        binding.deviceList.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }

        return binding.root
    }

    private fun fetchDevices() {
        databaseReference.child("device").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _binding?.let {
                    deviceList.clear()
                    if (snapshot.exists()) {
                        for (deviceSnap in snapshot.children) {
                            val devices = deviceSnap.getValue(Device::class.java)
                            deviceList.add(devices!!)
                        }
                    }
                    val rAdapter = DeviceAdapter(deviceList)
                    binding.deviceList.adapter = rAdapter
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "ERROR: $error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    @SuppressLint("InflateParams")
    private fun addDevice() {
        val view = layoutInflater.inflate(R.layout.dialog_add_device, null)
        val name = view.findViewById<EditText>(R.id.device_name)
        val clientID = view.findViewById<EditText>(R.id._clientID)
        val token = view.findViewById<EditText>(R.id._token)
//        val streaming = view.findViewById<EditText>(R.id._)
        val desc = view.findViewById<EditText>(R.id._description)

        val customViewDialog = CustomViewDialog()
        customViewDialog.Builder(context).apply {
            setTitle("ADD DEVICE")
            dialogWithTwoButtons()
            addCustomView(view)
            setLeftButtonColor("#FF0000".toColorInt())
            setRightButtonColor("#008000".toColorInt())
            onButtonClick {
                if (name != null && clientID != null && token != null) {
                    val deviceID = databaseReference.push().key!!
                    val manual = Manual("","","")
                    val auto = Autonomous("")
                    val mode = Mode(manual,auto)
                    val status = Status(mode,"","")
                    val deviceInfo = Device(
                        deviceID,
                        name.text.toString(),
                        clientID.text.toString(),
                        token.text.toString(),
                        status,
                        desc.text.toString()
                    )
                    databaseReference.child("device").child(deviceID)
                        .setValue(deviceInfo).addOnCompleteListener {
                            Toast.makeText(context, "New device added", Toast.LENGTH_SHORT).show()
                        }.addOnFailureListener {
                            Toast.makeText(context, "ERROR: ${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                }
                dismiss()
            }
            show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        /*--set t prevent memory leaks--*/
    }
}