package com.example.azimuth

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.azimuth.databinding.ActivityUpdateProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
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
//            updateDataUsers()
            updateProfile()
        }
    }

    private fun updateProfile() {
        val user = Firebase.auth.currentUser
        val name = binding.edtProfileName.text.toString()
        if (name.isEmpty()) binding.edtProfileName.error = "Please write your name!"
        val profileUpdates = userProfileChangeRequest {
            displayName = name
//            photoUri = Uri.parse("https://example.com/jane-q-user/profile.jpg")
        }

        user!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "User profile updated.")
                }
            }
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

//    private fun updateDataUsers() {
//        val name = binding.edtProfileName.text.toString()
//        val bio = binding.edtProfileBio.text.toString()
//        if (name.isEmpty()) binding.edtProfileName.error = "Please write your name!"
////        val userId = database.push().key!!
//        val uid = Firebase.auth.currentUser?.uid.toString()
//        val user = User(uid, name, bio)
//
//        database.child(uid).setValue(user)
//            .addOnCompleteListener {
//                Toast.makeText(this, "User's data updated Successful!", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//            }.addOnFailureListener {
////                Toast.makeText(this, "error ${it.message}", Toast.LENGTH_SHORT).show()
//                Toast.makeText(this, "Failed to update user's profile.", Toast.LENGTH_SHORT).show()
//            }
//
//    }
}