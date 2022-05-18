package com.example.semester_app.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.semester_app.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegistrationActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        binding.registerBtn.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.emailAddress.text.toString()
            val pass = binding.password.text.toString()

            if (name.isEmpty()) {
                binding.emailAddress.error = "Name is required."
                binding.name.requestFocus()

            } else if (email.isEmpty()) {
                binding.emailAddress.error = "Email was not be found."
                binding.emailAddress.requestFocus()

            } else if (pass.isEmpty()) {
                binding.password.error = "Need password."
                binding.password.requestFocus()

            } else {
                // User registered...
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Successful registration.", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this,MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Fail registration. ${it.exception}", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
    }
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}