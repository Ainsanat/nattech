package com.example.nattech.fragments

import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.nattech.GridAdapter
import com.example.nattech.GridViewModel
import com.example.nattech.R
import com.example.nattech.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import com.google.android.gms.auth.api.signin.GoogleSignInAccount as GoogleSignInAccount1


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment(){
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var courseGrid: GridView
    private lateinit var courseList: List<GridViewModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        val timeStamp = SimpleDateFormat("EEEE MMMM dd, yyyy", Locale.ENGLISH).format(Calendar.getInstance().time)
        binding.curDate.text = timeStamp

        courseGrid = binding.gridv
        courseList = ArrayList()

        courseList = courseList + GridViewModel("Battery", "78%", R.drawable.battery_icon)
        courseList = courseList + GridViewModel("Control Box", "100%", R.drawable.energy)
        courseList = courseList + GridViewModel("Level of Rotary", "BASE", R.drawable.setting)

        val courseAdapter = context?.let { GridAdapter(courseList = courseList, it) }
        courseGrid.adapter = courseAdapter
        courseGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            Toast.makeText(context, courseList[position].name + " selected", Toast.LENGTH_SHORT).show()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

