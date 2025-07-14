package com.example.azimuth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.azimuth.databinding.ActivityMainBinding
import com.example.azimuth.fragments.*


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