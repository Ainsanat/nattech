package com.example.azimuth.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.azimuth.Autonomous
import com.example.azimuth.Manual
import com.example.azimuth.Mode
import com.example.azimuth.R
import com.example.azimuth.Status
import com.example.azimuth.api.APIService
import com.example.azimuth.api.BasicAuthClient
import com.example.azimuth.databinding.FragmentControlBinding
import com.faizkhan.mjpegviewer.MjpegView
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class ControlFragment : Fragment() {
    private var _binding: FragmentControlBinding? = null
    private val binding get() = _binding!!

    private lateinit var databaseReference: DatabaseReference
    private val args: ControlFragmentArgs by navArgs()

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlBinding.inflate(inflater, container, false)

        val user = Firebase.auth.currentUser?.uid.toString()

        databaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child(user).child("device")
                .child(args.id).child("status")
        binding.txtDeviceName.text = args.name

        binding.cAutonomous.setOnClickListener {
            findNavController().navigate(R.id.action_controlFragment_to_autonomousFragment)
        }

        binding.segmented {
            initialCheckedIndex = 0
            onSegmentChecked { segment ->
                when (segment.text) {
                    "BASE LEVEL" -> adjustment("base")
                    "FIRST LEVEL" -> adjustment("first")
                    "SECOND LEVEL" -> adjustment("second")
                }
                Log.d("creageek:segmented", "Segment ${segment.text} checked")
            }
            // notifies when segment was unchecked
            onSegmentUnchecked { segment ->
                Log.d("creageek:segmented", "Segment ${segment.text} unchecked")
            }
            // notifies when segment was rechecked
            onSegmentRechecked { segment ->
                Log.d("creageek:segmented", "Segment ${segment.text} rechecked")
            }
        }

        binding.btnForward.setOnTouchListener { _, p1 ->
            when (p1!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.btnForward.setBackgroundResource(R.drawable.button_press)
                    movement("forward")
                }

                MotionEvent.ACTION_UP -> {
                    binding.btnForward.setBackgroundResource(R.drawable.button_enable)
                    movement("stop")
                }
            }
            true
        }
        binding.btnBackward.setOnTouchListener { _, p1 ->
            when (p1!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.btnBackward.setBackgroundResource(R.drawable.button_press)
                    movement("backward")
                }

                MotionEvent.ACTION_UP -> {
                    binding.btnBackward.setBackgroundResource(R.drawable.button_enable)
                    movement("stop")
                }
            }
            true
        }
        binding.btnTurnleft.setOnTouchListener { _, p1 ->
            when (p1!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.btnTurnleft.setBackgroundResource(R.drawable.button_press)
                    movement("left")
                }

                MotionEvent.ACTION_UP -> {
                    binding.btnTurnleft.setBackgroundResource(R.drawable.button_enable)
                    movement("stop")

                }
            }
            true
        }
        binding.btnTurnright.setOnTouchListener { _, p1 ->
            when (p1!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.btnTurnright.setBackgroundResource(R.drawable.button_press)
                    movement("right")
                }

                MotionEvent.ACTION_UP -> {
                    binding.btnTurnright.setBackgroundResource(R.drawable.button_enable)
                    movement("stop")
                }
            }
            true
        }

        binding.btnFrontCam.setOnClickListener {
            binding.btnFrontCam.isSelected = !binding.btnFrontCam.isSelected
            if (binding.btnFrontCam.isSelected) {
                videoStreaming()
                Toast.makeText(context, "CAMERA ON", Toast.LENGTH_SHORT).show()
            } else if (!binding.btnFrontCam.isSelected) {
                stopStreaming()
                Toast.makeText(context, "CAMERA OFF", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnRotary.setOnClickListener {
            binding.btnRotary.isSelected = !binding.btnRotary.isSelected
            if (binding.btnRotary.isSelected) {
                rotary("on")
//                binding.btnRotary.setBackgroundColor(R.color.Aerospace_International_Orange)
                Toast.makeText(context, "ON", Toast.LENGTH_SHORT).show()
            } else if (!binding.btnRotary.isSelected) {
                rotary("off")
//                binding.btnRotary.setBackgroundColor(R.color.Bright_Gray)
                Toast.makeText(context, "OFF", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnOnOff.setOnClickListener {

        }

        return binding.root
    }

    private fun videoStreaming() {
        binding.streamCam.apply {
            isAdjustHeight = true
            mode1 = MjpegView.MODE_FIT_WIDTH
            //rotation = 180.0F
            //setUrl("https://bma-itic1.iticfoundation.org/mjpeg2.php?camid=test")
            setUrl("http://172.20.10.2:81/stream")
            isRecycleBitmap1 = true
            startStream()
        }
    }

    private fun stopStreaming() {
        binding.streamCam.apply {
            isAdjustHeight = true
            mode1 = MjpegView.MODE_FIT_WIDTH
            rotation = 180.0F
            //setUrl("https://bma-itic1.iticfoundation.org/mjpeg2.php?camid=test")
            setUrl("http://172.20.10.6:81/stream")
            isRecycleBitmap1 = true
            stopStream()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun movement(directions: String) {
        val updateMovement = hashMapOf<String, Any>(
            "movement" to directions
        )
        databaseReference.child("mode").child("manual").updateChildren(updateMovement)
            .addOnCompleteListener {
                Toast.makeText(context, "Movement", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
            Toast.makeText(context, "ERROR: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun adjustment(level: String) {
        val updateLeveling = hashMapOf<String, Any>(
            "adjustment" to level
        )
        databaseReference.child("mode").child("manual").updateChildren(updateLeveling)
            .addOnCompleteListener {
                Toast.makeText(context, "Adjust", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
            Toast.makeText(context, "ERROR: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rotary(state: String) {
        val updateRotary = hashMapOf<String, Any>(
            "rotary" to state
        )
        databaseReference.child("mode").child("manual").updateChildren(updateRotary)
            .addOnCompleteListener {
                Toast.makeText(context, "Adjust", Toast.LENGTH_SHORT).show()

            }.addOnFailureListener {
            Toast.makeText(context, "ERROR: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

}