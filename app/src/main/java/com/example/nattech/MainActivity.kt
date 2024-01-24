package com.example.nattech

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.nattech.databinding.ActivityMainBinding
import com.example.nattech.fragments.*
import com.google.android.material.textfield.TextInputEditText


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val homeFragment = HomeFragment()
        val chartFragment = ChartFragment()
//        val controlFragment = ControlFragment()
        val notificationFragment = NotificationFragment()
        val profileFragment = ProfileFragment()

        makeCurrentFragment(homeFragment)

        binding.bottomNavigation.setOnItemSelectedListener{
            when (it.itemId){
                R.id.home -> makeCurrentFragment(homeFragment)
                R.id.chart -> makeCurrentFragment(chartFragment)
//                R.id.control -> makeCurrentFragment(controlFragment)
                R.id.notification -> makeCurrentFragment(notificationFragment)
                R.id.profile -> makeCurrentFragment(profileFragment)

            }
            true
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.container, fragment)
            commit()
    }

}