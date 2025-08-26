package com.example.azimuth.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.azimuth.R
import com.example.azimuth.SignInActivity
import com.example.azimuth.User
import com.example.azimuth.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.sjapps.library.customdialog.CustomViewDialog
import androidx.core.net.toUri

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        //Coding below here der jan
        binding.apply {
            profileLogout.setOnClickListener {
//                Toast.makeText(context, "Are you sure ti logout", Toast.LENGTH_SHORT).show()
                Firebase.auth.signOut()
                val intent = Intent(activity, SignInActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear back stack
                startActivity(intent)
                activity?.finish()
                //add re-confirm to Logout
            }
        }
        binding.btnEditProfile.setOnClickListener {
//            showDialogEditProfile()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun showDialogEditProfile(){
//        val view = layoutInflater.inflate(R.layout.dialog_edit_profile, null)
////        val imageUser = view.findViewById<ImageView>(R.id.)
//        val displayName = view.findViewById<EditText>(R.id.edtDisplayName)
//        val bioUser = view.findViewById<EditText>(R.id.edtBioProfile)
//
//        val customViewDialog = CustomViewDialog()
//        customViewDialog.Builder(context).apply{
//            setTitle("Edit Your Profile")
//            dialogWithTwoButtons()
//            addCustomView(view)
//            onButtonClick {
//                if (displayName != null && bioUser != null){
//                    val name = displayName.text.toString()
//                    val bio = bioUser.text.toString()
//                    saveUserProfile(name, bio)
//                }
//                dismiss()
//            }
//            show()
//        }
//
//    }
//    private fun saveUserProfile(name: String, bio: String){
//        auth = FirebaseAuth.getInstance()
//        val uid = auth.currentUser?.uid
//        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
//
//        val user = User(name, bio)
//        if (uid != null){
//            databaseReference.child(uid).setValue(user).addOnCompleteListener {
//                if (it.isSuccessful){
////                    uploadProfilePic()
//                } else {
//                    Toast.makeText(context, "Failed to update profile", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }

    private fun uploadProfilePic() {
        val packageName = context?.packageName

        imageUri = "android.resource://$packageName/${R.drawable.profile_signup}".toUri()
        storageReference = FirebaseStorage.getInstance().getReference("Users/" + auth.currentUser?.uid)
        storageReference.putFile(imageUri).addOnSuccessListener {
            Toast.makeText(context, "Profile's image successful update.", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener{
            Toast.makeText(context, "Failed to update profile's image", Toast.LENGTH_SHORT).show()
        }
    }
}