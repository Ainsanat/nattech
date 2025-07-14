package com.example.azimuth

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.azimuth.databinding.ActivitySignInBinding
import com.example.azimuth.fragments.HomeFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    private val mFragmentManager = supportFragmentManager
    private val mFragmentTransaction = mFragmentManager.beginTransaction()
    private val mFragment = HomeFragment()

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignin.setOnClickListener {
            val email = binding.signinUsername.text.toString()
            val password = binding.signinPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
//                            updateUI()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(
                                baseContext,
                                "Authentication failed.",
                                Toast.LENGTH_SHORT,
                            ).show()
                        }
                    }
            }

        }

    }

    private fun updateUI() {

        val user = auth.currentUser
        user?.let {
            val name = it.displayName
            if (name != null) {
                // Use the displayName
                Toast.makeText(this, "$name", Toast.LENGTH_SHORT).show()
            } else {
                // displayName might be null if not set or not available from the sign-in method
                // Consider extracting from email as a fallback or prompt user to set a display name
                Toast.makeText(this, "Failed to fetch name user", Toast.LENGTH_SHORT).show()
            }
//            val email = it.email
//            val photoUrl = it.photoUrl

            // Check if user's email is verified
//            val emailVerified = it.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
//            val uid = it.uid


            val bundle = Bundle()
            bundle.putString("displayName", name.toString())
//            bundle.putString("email", email)
            mFragment.arguments = bundle
            mFragmentTransaction.add(R.id.user_name, mFragment).commit()

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
