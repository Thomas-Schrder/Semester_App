package com.example.semester_app.presentation.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.semester_app.databinding.ActivityLoginBinding
import com.example.semester_app.presentation.activities.RegistrationActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mAuth = FirebaseAuth.getInstance()

        binding.loginBtn.setOnClickListener {
          val email = binding.emailAddress.text.toString()
            val pass = binding.password.text.toString()

            if (email.isEmpty()) {
                binding.emailAddress.error = "Email was not be found."
                binding.emailAddress.requestFocus()

            } else if(pass.isEmpty()) {
                binding.password.error = "Need password."
                binding.password.requestFocus()

        } else {

            // User login...
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Successful login.", Toast.LENGTH_SHORT).show()
                        finish()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Fail login. ${it.exception}", Toast.LENGTH_LONG).show()

                    }
                }
            }
        }
        binding.goToRegistration.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
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
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}