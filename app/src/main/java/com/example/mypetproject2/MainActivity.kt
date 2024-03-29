package com.example.mypetproject2

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mypetproject2.databinding.ActivityMain2Binding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, SplashScreenFragment())
            .commit()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            hideBottomAppBar(destination, navView)
        }
    }

    private fun hideBottomAppBar(destination: NavDestination, navView: BottomNavigationView) {
        when (destination.id) {
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications -> navView.visibility = View.VISIBLE
            else -> navView.visibility = View.GONE
        }
    }
}