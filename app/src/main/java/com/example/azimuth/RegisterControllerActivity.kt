package com.example.azimuth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.azimuth.databinding.ActivityRegisterControllerBinding
import com.example.azimuth.api.BasicAuthClient

class RegisterControllerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterControllerBinding
    private val registerControl = BasicAuthClient<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterControllerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegisterController.setOnClickListener{
            val clientID: String = binding.registerClientID.text.toString()
            val token: String = binding.registerToken.text.toString()
            registerControl.setValue(clientID, token)

            Toast.makeText(this, registerControl.clientID.toString(), Toast.LENGTH_SHORT).show()
            Toast.makeText(this, registerControl.token.toString(), Toast.LENGTH_SHORT).show()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}