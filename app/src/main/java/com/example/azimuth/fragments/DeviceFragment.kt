package com.example.azimuth.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.azimuth.Device
import com.example.azimuth.R
import com.example.azimuth.databinding.FragmentDeviceBinding
import com.sjapps.library.customdialog.CustomViewDialog
import androidx.core.graphics.toColorInt
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class DeviceFragment : Fragment() {
    private var _binding: FragmentDeviceBinding? = null
    private val binding get() = _binding!!

    /*
    private lateinit var mSQLiteHelper: SQLiteHelper
    private lateinit var mSQLiteDatabase: SQLiteDatabase
    private lateinit var machineData: Triple<String?, String?, String?>

     */
    private lateinit var databaseReference: DatabaseReference
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var deviceInfo: Device

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceBinding.inflate(inflater, container, false)

        val user = Firebase.auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user)
        /*
                mSQLiteHelper = SQLiteHelper.getInstance(context)
                mSQLiteDatabase = mSQLiteHelper.writableDatabase

         */
        mRecyclerView = binding.deviceList
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        val itemDecor = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        mRecyclerView.addItemDecoration(itemDecor)
//        readDatabaseToRecyclerview()

//        binding.btnAddMachine.setOnClickListener() {
//            showDialogAddDevice()
//        }
        binding.btnAddDevice.setOnClickListener {
            addDevice()
        }

        return binding.root
    }

    private fun addDevice() {
        val view = layoutInflater.inflate(R.layout.dialog_add_device, null)
        val name = view.findViewById<EditText>(R.id.device_name)
        val clientID = view.findViewById<EditText>(R.id._clientID)
        val token = view.findViewById<EditText>(R.id._token)
        val desc = view.findViewById<EditText>(R.id._description)

        val customViewDialog = CustomViewDialog()
        customViewDialog.Builder(context).apply {
            setTitle("ADD DEVICE")
            dialogWithTwoButtons()
            addCustomView(view)
            setLeftButtonColor("#FF0000".toColorInt())
            setRightButtonColor("#008000".toColorInt())
            onButtonClick {
                if (name != null && clientID != null && token != null && desc != null) {
                    deviceInfo = Device(
                        name.text.toString(),
                        clientID.text.toString(),
                        token.text.toString(),
                        "",
                        "",
                        desc.text.toString()
                    )
                    insertDataToFirebase()
                }
                dismiss()
            }
            show()
        }
    }

    private fun insertDataToFirebase() {

        val deviceID = databaseReference.push().key!!
        val device = deviceInfo

        databaseReference.child("device").child(deviceID)
            .setValue(device).addOnCompleteListener {
            Toast.makeText(context, "New device added", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(context, "ERROR: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun readFirebaseToRecyclerView() {
        TODO("Not yet implemented")
    }

    /*
    fun readDatabaseToRecyclerview() {
        val sql = "SELECT * FROM machine ORDER BY timestamp DESC"
        val cursor = mSQLiteDatabase.rawQuery(sql, null)
        val adapter = CardAdapter(cursor)
        mRecyclerView.adapter = adapter
    }


    private fun showDialogAddDevice() {
        val view = layoutInflater.inflate(R.layout.dialog_add_device, null)
        val machineName = view.findViewById<EditText>(R.id.device_name)
        val machineClientID = view.findViewById<EditText>(R.id._clientID)
        val machineToken = view.findViewById<EditText>(R.id._token)

        val customViewDialog = CustomViewDialog()
        customViewDialog.Builder(context).apply {
            setTitle("ADD MACHINE")
            dialogWithTwoButtons()
            addCustomView(view)
            setLeftButtonColor(Color.parseColor("#FF0000"))
            setRightButtonColor(Color.parseColor("#008000"))
            onButtonClick {
                if (machineName != null && machineClientID != null && machineToken != null) {
                    machineData = Triple(
                        machineName.text.toString(),
                        machineClientID.text.toString(),
                        machineToken.text.toString()
                    )
                    insertDatabase()
                }
                dismiss()
            }
            show()
        }
    }

    private fun insertDatabase() {
        var now = System.currentTimeMillis()
        var sql =
            """INSERT INTO machine (_id, name, clientID, token, timestamp) VALUES (?, ?, ?, ?, ?)"""
        var args = arrayOf(null, machineData.first, machineData.second, machineData.third, now)
        mSQLiteDatabase.execSQL(sql, args)
        readDatabaseToRecyclerview()
    }
     */

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}