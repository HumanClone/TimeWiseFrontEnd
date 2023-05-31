package com.example.timewisefrontend.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.timewisefrontend.R

class RegisterActivity : AppCompatActivity() {

    private lateinit var inputUser : EditText
    private lateinit var inputUserJob : EditText
    private lateinit var inputLoginEmail : EditText
    private lateinit var inputPassword : EditText
    private lateinit var inputConfirmPassword : EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        inputUser = findViewById(R.id.inputUser)
        inputUserJob = findViewById(R.id.inputUserJob)
        inputLoginEmail = findViewById(R.id.inputLoginEmail)
        inputPassword = findViewById(R.id.inputPassword)
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword)



        val registerButton = findViewById<Button>(R.id.btnRegister)
        registerButton.setOnClickListener{
            Toast.makeText(this@RegisterActivity, "Registered Successful!",Toast.LENGTH_SHORT).show()
            val intent = Intent(this@RegisterActivity, MainMenuActivity::class.java)
            startActivity(intent)
        }
        val loginButton = findViewById<TextView>(R.id.alreadyHaveAccount)
        loginButton.setOnClickListener{
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        checkCrededentials()
    }
    private fun checkCrededentials(){
        val username = inputUser.text.toString()
        val userjob = inputUserJob.text.toString()
        val email = inputLoginEmail.text.toString()
        val password = inputPassword.text.toString()
        val configurePassword = inputConfirmPassword.text.toString()

        if (username.isEmpty() || username.length <7)
        {
            showError(inputUser, "Your username is not valid!")
        }
        else if (userjob.isEmpty())
        {
            showError(inputUserJob, "You havent entered a job")
        }
        else if (email.isEmpty() || !email.contains("@"))
        {
            showError(inputLoginEmail, "Your Email is not valid!")
        }
        else if (password.isEmpty() || password.length <7)
        {
            showError(inputPassword, "Password must be 7 characters")
        }
        else if (configurePassword.isEmpty() || configurePassword !=password)
        {
            showError(inputConfirmPassword, "Password does not match")
        }
        else
        {
            Toast.makeText(this@RegisterActivity, "Call Registration Method", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showError(input : EditText, errorMessage: String )
    {
        input.error = errorMessage
        input.requestFocus()
    }
}