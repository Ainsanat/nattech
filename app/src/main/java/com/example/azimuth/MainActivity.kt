package com.example.azimuth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.azimuth.databinding.ActivityMainBinding
import com.example.azimuth.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*
        val homeFragment = HomeFragment()
        val deviceFragment = DeviceFragment()
//        val controlFragment = ControlFragment()
        val notificationFragment = NotificationFragment()
        val profileFragment = ProfileFragment()

        makeCurrentFragment(homeFragment)

        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home -> makeCurrentFragment(homeFragment)
                R.id.device -> makeCurrentFragment(deviceFragment)
//                R.id.control -> makeCurrentFragment(controlFragment)
                R.id.notify -> makeCurrentFragment(notificationFragment)
                R.id.profile -> makeCurrentFragment(profileFragment)

            }
            true
        }

         */
//        val navigationView: BottomNavigationView = binding.bottomNavigation
//        val navController = findNavController(R.id.fragmentContainerView)
//        navigationView.setupWithNavController(navController)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
    }

    /*
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            commit()
        }

     */
}