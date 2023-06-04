package com.example.timewisefrontend.screens

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timewisefrontend.R
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.User
import com.example.timewisefrontend.models.UserDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*

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
                    UserDetails.email=email
                    val userf = Firebase.auth.currentUser
                    userf.let{
                        UserDetails.name= it!!.displayName.toString()
                        UserDetails.userId= it.uid
                    }
                    getUserNorm()

                } else {
                    mLoadingBar.dismiss()
                    val message = task.exception?.message ?: "Login failed. Please try again."
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
    }



    private fun getUserNorm()
    { Log.d("testing","click")
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {


                val call: User = timeWiseApi.getUserNorm(UserDetails.userId)
                UserDetails.job=call.Job!!
                if (!call.Max!!.toString().isNullOrEmpty())
                {
                    UserDetails.max=call.Max!!
                }
                if (!call.Min.toString().isNullOrEmpty())
                {
                    UserDetails.min=call.Min!!
                }
                mLoadingBar.dismiss()
                Log.d("testing", call.toString())

                val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                startActivity(intent)
                finish()
            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }
            catch (e:java.lang.NullPointerException)
            {
                mLoadingBar.dismiss()
                UserDetails.min=0
                UserDetails.max=0
                val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }





}
