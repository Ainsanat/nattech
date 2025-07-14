package com.example.azimuth.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.azimuth.R
import com.example.azimuth.api.APIService
import com.example.azimuth.api.BasicAuthClient
import com.example.azimuth.databinding.FragmentControlBinding
import com.example.retrofit.api.*
import com.faizkhan.mjpegviewer.MjpegView
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback

class ControlFragment : Fragment() {
    private var _binding: FragmentControlBinding? = null
    private val binding get() = _binding!!

    @SuppressLint("ClickableViewAccessibility", "ResourceAsColor")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentControlBinding.inflate(inflater, container, false)

        binding.segmented{
            initialCheckedIndex = 0

            // notifies when segment was checked
            onSegmentChecked { segment ->
                when (segment.text){
                    "BASE LEVEL" -> leveling("base")
                    "FIRST LEVEL" -> leveling("first")
                    "SECOND LEVEL" -> leveling("second")
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
                    movement("turnleft")
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
                    movement("turnright")
                }
                MotionEvent.ACTION_UP -> {
                    binding.btnTurnright.setBackgroundResource(R.drawable.button_enable)
                    movement("stop")
                }
            }
            true
        }
        //edit to change color button
        binding.btnFrontCam.setOnClickListener{
            binding.btnFrontCam.isSelected = !binding.btnFrontCam.isSelected
            if (binding.btnFrontCam.isSelected){
                videoStreaming()
                Toast.makeText(context,"CAMERA ON",Toast.LENGTH_SHORT).show()
            } else if (!binding.btnFrontCam.isSelected){
                stopStreaming()
                Toast.makeText(context,"CAMERA OFF",Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnAuto.setOnClickListener {
            //call map api
            val mapsFragment = MapsFragment()
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.container, mapsFragment).commit()
        }
        binding.btnRotary.setOnClickListener {
            binding.btnRotary.isSelected = !binding.btnRotary.isSelected
            if (binding.btnRotary.isSelected){
                shoveling("on")
                Toast.makeText(context,"ON",Toast.LENGTH_SHORT).show()
            } else if (!binding.btnRotary.isSelected){
                shoveling("off")
                Toast.makeText(context,"OFF",Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnOnOff.setOnClickListener {
            login()
        }

        // Inflate the layout for this fragment
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
        val body: RequestBody = directions.toRequestBody("text/plain;charset=utf-8".toMediaType())
        val call = BasicAuthClient<APIService>().create(APIService::class.java)
        call.movement(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.i("API", "Movement Success")
                } else {
                    Log.e("API", "Movement Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API", t.message, t)
            }
        })
    }
//    private fun autonomous(directions: String) {
//        val body: RequestBody = directions.toRequestBody("text/plain;charset=utf-8".toMediaType())
//        val call = BasicAuthClient<APIService>().create(APIService::class.java)
//        call.autonomous(body).enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(
//                call: Call<ResponseBody>,
//                response: retrofit2.Response<ResponseBody>
//            ) {
//                if (response.isSuccessful) {
//                    Log.i("API", "Autonomous Mode Online")
//                } else {
//                    Log.e("API", "Autonomous Mode Error: ${response.code()} ${response.message()}")
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                Log.e("API", t.message, t)
//            }
//        })
//    }

    private fun leveling(directions: String) {
        val body: RequestBody = directions.toRequestBody("text/plain;charset=utf-8".toMediaType())
        val call = BasicAuthClient<APIService>().create(APIService::class.java)
        call.leveling(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.i("API", "Adjust the level")
                } else {
                    Log.e("API", "Adjust Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API", t.message, t)
            }
        })
    }

    private fun login(){
        val call = BasicAuthClient<APIService>().create(APIService::class.java).login()
        call.enqueue(object: Callback<Response> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                val stringResponse = response.body()?.toString()
                if (response.isSuccessful) {
                    Log.i("API", "Login Success")
                    println(stringResponse)
                    //view?.btn_on_off?.setBackgroundColor(ContextCompat.getColor(context!!, R.color.green))
                    //view?.btn_on_off?.text = "ON"
                    binding.btnOnOff.setBackgroundColor(ContextCompat.getColor(context!!, R.color.green))
                    binding.btnOnOff.text = "ON"
                } else {
                    Log.e("API", "Login Error: ${response.code()} ${response.message()}")
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                Log.e("API", t.message, t)
            }
        })
    }

    private fun shoveling(directions: String) {
        val body: RequestBody = directions.toRequestBody("text/plain;charset=utf-8".toMediaType())
        val call = BasicAuthClient<APIService>().create(APIService::class.java)
        call.shoveling(body).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Log.i("API", "Rotaty Working")
                } else {
                    Log.e("API", "Rotary Error: ${response.code()} ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("API", t.message, t)
            }
        })
    }

}