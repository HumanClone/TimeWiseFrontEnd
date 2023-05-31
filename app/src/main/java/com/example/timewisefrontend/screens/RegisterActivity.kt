package com.example.timewisefrontend.screens

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.timewisefrontend.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.jetbrains.annotations.NonNls

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputUser: EditText
    private lateinit var inputUserJob: EditText
    private lateinit var inputLoginEmail: EditText
    private lateinit var inputPassword: EditText
    private lateinit var inputConfirmPassword: EditText
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mLoadingBar: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputUser = findViewById(R.id.inputUser)
        inputUserJob = findViewById(R.id.inputUserJob)
        inputLoginEmail = findViewById(R.id.inputLoginEmail)
        inputPassword = findViewById(R.id.inputPassword)
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword)
        mAuth = FirebaseAuth.getInstance()
        mLoadingBar = ProgressDialog(this)

        val registerButton = findViewById<Button>(R.id.btnRegister)
        registerButton.setOnClickListener {
            if (checkCredentials()) {
                val email = inputLoginEmail.text.toString()
                val password = inputPassword.text.toString()
                registerUser(email, password)
            }
        }
        val loginButton = findViewById<TextView>(R.id.alreadyHaveAccount)
        loginButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun checkCredentials(): Boolean {
        val username = inputUser.text.toString()
        val userJob = inputUserJob.text.toString()
        val email = inputLoginEmail.text.toString()
        val password = inputPassword.text.toString()
        val confirmPassword = inputConfirmPassword.text.toString()

        if (username.isEmpty() || username.length < 7) {
            showError(inputUser, "Your username is not valid, please enter more then 7 letters!")
            return false
        } else if (userJob.isEmpty()) {
            showError(inputUserJob, "You haven't entered a job")
            return false
        } else if (email.isEmpty() || !email.contains("@")) {
            showError(inputLoginEmail, "Your Email is not valid!")
            return false
        } else if (password.isEmpty() || password.length < 7) {
            showError(inputPassword, "Password must be at least 7 characters")
            return false
        } else if (!password.matches(Regex(".*\\d.*"))) {
            showError(inputPassword, "Password must contain at least one digit")
            return false
        } else if (confirmPassword.isEmpty() || confirmPassword != password) {
            showError(inputConfirmPassword, "Password does not match")
            return false
        }

        return true
    }

    private fun showError(input: EditText, errorMessage: String) {
        input.error = errorMessage
        input.requestFocus()
    }

    private fun registerUser(email: String, password: String) {
        mLoadingBar.setTitle("Registering")
        mLoadingBar.setMessage("Please wait, while checking your credentials")
        mLoadingBar.setCanceledOnTouchOutside(false)
        mLoadingBar.show()

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mLoadingBar.dismiss()
                    Toast.makeText(
                        this@RegisterActivity,
                        "Registered Successfully!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this@RegisterActivity, MainMenuActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    mLoadingBar.dismiss()
                    val exception = task.exception as? FirebaseAuthException
                    Toast.makeText(
                        this@RegisterActivity, "Registration failed: " +
                                "${exception?.message}", Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
