package com.example.timewisefrontend.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.timewisefrontend.R
import com.example.timewisefrontend.databinding.ActivityLoginBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class LoginActivity : AppCompatActivity() {

    private lateinit var inputLoginEmail: EditText
    private lateinit var inputUserPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputLoginEmail = findViewById(R.id.inputLoginEmail)
        inputUserPassword = findViewById(R.id.inputUserPassword)

        val registerButton = findViewById<Button>(R.id.btnNewRegister)
        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        val loginButton = findViewById<Button>(R.id.btnLogin)
        loginButton.setOnClickListener {
            if (checkCredentials()) {
                val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun checkCredentials(): Boolean {
        val email = inputLoginEmail.text.toString()
        val password = inputUserPassword.text.toString()

        if (email.isEmpty() || !email.contains("@")) {
            showError(inputLoginEmail, "Your Email is not valid!")
            return false
        } else if (password.isEmpty() || password.length < 7) {
            showError(inputUserPassword, "Password must be 7 characters")
            return false
        }
        else
        {
            Toast.makeText(this@LoginActivity, "Call Login Method", Toast.LENGTH_SHORT).show()
            return true
        }
    }

    private fun showError(input: EditText, errorMessage: String) {
        input.error = errorMessage
        input.requestFocus()
    }
}
