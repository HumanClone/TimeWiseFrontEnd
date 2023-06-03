package com.example.timewisefrontend.screens

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.timewisefrontend.R
import com.example.timewisefrontend.databinding.ActivityLoginBinding
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var inputLoginEmail: EditText
    private lateinit var inputUserPassword: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mLoadingBar: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        inputLoginEmail = findViewById(R.id.inputLoginEmail)
        inputUserPassword = findViewById(R.id.inputUserPassword)
        mAuth = FirebaseAuth.getInstance()
        mLoadingBar = ProgressDialog(this)

        val registerButton = findViewById<Button>(R.id.btnNewRegister)
        registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        val loginButton = findViewById<Button>(R.id.btnLogin)
        loginButton.setOnClickListener {
            if (checkCredentials()) {
                val email = inputLoginEmail.text.toString()
                val password = inputUserPassword.text.toString()
                loginUser(email, password)
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
            return true
        }

    private fun showError(input: EditText, errorMessage: String) {
        input.error = errorMessage
        input.requestFocus()
    }
    private fun loginUser(email: String, password: String) {
        mLoadingBar.setTitle("Logging In")
        mLoadingBar.setMessage("Please wait, while we are logging you in")
        mLoadingBar.setCanceledOnTouchOutside(false)
        mLoadingBar.show()

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    mLoadingBar.dismiss()
                    Toast.makeText(
                        this@LoginActivity,
                        "Logged in Successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    mLoadingBar.dismiss()
                    val message = task.exception?.message ?: "Login failed. Please try again."
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
