package com.example.azimuth

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.azimuth.databinding.ActivityUpdateProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var databaseRef: DatabaseReference
    private lateinit var uri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseRef = FirebaseDatabase.getInstance().getReference("Users")

        val pickMedia =
            registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { Uri ->
                if (Uri != null) {
                    uri = Uri
                    binding.imgEditProfile.setImageURI(Uri)
                } else {
//
                }
            }

        binding.pickImage.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        binding.btnUpdateProfile.setOnClickListener {
            uploadImageToFirebaseStorage(uri)
        }
    }


    private fun uploadImageToFirebaseStorage(imageUri: Uri) {
        val userId = Firebase.auth.currentUser?.uid
        if (userId != null) {
            val storageRef = Firebase.storage.reference.child("profile_pictures/$userId.jpg")
            storageRef.putFile(imageUri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Update the user's photoUrl with this downloadUri
                        updateUserPhotoUrl(downloadUri)
                    }
                }
                .addOnFailureListener {
                    // Handle upload failure
                }
        }
    }

    private fun updateUserPhotoUrl(photoDownloadUri: Uri) {
        val user = Firebase.auth.currentUser
        val name = binding.edtProfileName.text.toString()
        if (name.isEmpty()) binding.edtProfileName.error = "Please fill your name"

        val newUser = User(null, null, null)
        databaseRef.child(user?.uid.toString()).setValue(newUser)
            .addOnCompleteListener {
                Toast.makeText(this, "Add new user successful.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "ERROR: ${it.message}", Toast.LENGTH_SHORT).show()
            }

        if (user != null) {
            val profileUpdates = userProfileChangeRequest {
                displayName = name
                photoUri = photoDownloadUri
            }
            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User profile updated successfully
                        Log.d(TAG, "User profile updated")
                    } else {
                        // Handle profile update failure
                    }
                }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

}