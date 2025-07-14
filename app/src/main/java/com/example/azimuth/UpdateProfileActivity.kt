package com.example.azimuth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.azimuth.databinding.ActivityUpdateProfileBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Users")

        binding.btnUpdateProfile.setOnClickListener {
            updateDataUsers()
        }
    }

    private fun updateDataUsers() {
        val name = binding.edtProfileName.text.toString()
        val bio = binding.edtProfileBio.text.toString()
        if (name.isEmpty()) binding.edtProfileName.error = "Please write your name!"
        val userId = database.push().key!!
        val user = User(userId, name, bio)

        database.child(userId).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this, "User's data updated Successful!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener {
//                Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, "Failed to update user's profile.", Toast.LENGTH_SHORT).show()
            }

    }
}