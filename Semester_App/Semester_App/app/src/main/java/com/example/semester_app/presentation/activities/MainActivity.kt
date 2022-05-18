package com.example.semester_app.presentation.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI

import com.example.semester_app.R
import com.example.semester_app.databinding.ActivityMainBinding
import com.example.semester_app.presentation.fragments.HomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = Navigation.findNavController(this, R.id.frag_host)
        NavigationUI.setupWithNavController(bottomNavigation,navController)

 ////      binding.userName.text = "Welcome ${mAuth.currentUser!!.email}"
   //     binding.logoutBtn.setOnClickListener {
   //         mAuth.signOut()
   //         updateUI(mAuth.currentUser)
    //    }

    }

    override fun onStart() {
        super.onStart()
    //    Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

        private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser == null) {
            finish()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}