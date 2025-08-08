package com.example.azimuth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.toColorInt
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.azimuth.databinding.FragmentInfoDeviceBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sjapps.library.customdialog.CustomViewDialog

class InfoDeviceFragment : Fragment() {
    private var _binding: FragmentInfoDeviceBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference
    private val args: InfoDeviceFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInfoDeviceBinding.inflate(inflater, container, false)

        binding.apply {
            updateNameDevice.setText(args.name)
            updateClientDevice.setText(args.clientID)
            updateTokenDevice.setText(args.token)
            updateStreamingDevice.setText(args.streamingURI)
            updateDescriptionDevice.setText(args.description)
        }

        val user = Firebase.auth.currentUser?.uid.toString()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(user)

        binding.updateDevice.setOnClickListener {
            updateDataDevice()
            findNavController().navigate(R.id.action_infoDeviceFragment_to_device)
        }
        binding.deleteDevice.setOnClickListener {
            deleteDevice()

        }

        return binding.root
    }

    private fun deleteDevice() {
        val customViewDialog = CustomViewDialog()
        customViewDialog.Builder(context).apply {
            setTitle("DELETE ${args.name}")
            dialogWithTwoButtons()
            setLeftButtonColor("#FF0000".toColorInt())
            setRightButtonColor("#008000".toColorInt())
            onButtonClick {
                databaseReference.child("device").child(args.id).removeValue().addOnSuccessListener {
                    Toast.makeText(context, "Delete ${args.name} successful", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_infoDeviceFragment_to_device)
                }.addOnFailureListener {
                    Toast.makeText(context, "Delete ${args.name} failed", Toast.LENGTH_SHORT).show()
                }
                dismiss()
            }
            show()
        }
    }


    private fun updateDataDevice() {
        val update = hashMapOf<String, Any>(
            "name" to binding.updateNameDevice.text.toString(),
            "description" to binding.updateDescriptionDevice.text.toString()
        )
        databaseReference.child("device").child(args.id).updateChildren(update)
            .addOnCompleteListener {
                Toast.makeText(context, "Update device successful", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
                Toast.makeText(context, "ERROR: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


}