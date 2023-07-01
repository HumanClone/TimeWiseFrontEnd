package com.example.timewisefrontend.screens

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.timewisefrontend.R
import com.example.timewisefrontend.api.RetrofitHelper
import com.example.timewisefrontend.api.TimeWiseApi
import com.example.timewisefrontend.models.Category
import com.example.timewisefrontend.models.TimeSheet
import com.example.timewisefrontend.models.User
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.snackbar.Snackbar
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
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

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
                    val message =  "Incorrect Credentials. Please Try Again "
                    Toast.makeText(this@LoginActivity, message, Toast.LENGTH_SHORT).show()
                    inputLoginEmail.error="Inncorrect Email or Password"
                }
            }
    }



    private fun getUserNorm()
    { Log.d("testing","click")
        getUserCategoriesNorm()
        getUserTSNorm()
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

                Log.d("testing", call.toString())

                val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                startActivity(intent)
                mLoadingBar.dismiss()
                runOnUiThread(Runnable {
                    Toast.makeText(this@LoginActivity, "Welcome Back ${UserDetails.name}", Toast.LENGTH_SHORT).show()
                })
                finish()
            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }
            catch (e:java.lang.NullPointerException)
            {

                UserDetails.min=0
                UserDetails.max=0
                val intent = Intent(this@LoginActivity, MainMenuActivity::class.java)
                startActivity(intent)
                mLoadingBar.dismiss()
                finish()
            }
        }
    }

    private fun getUserCategoriesNorm()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {


                val call:List<Category> = timeWiseApi.getAllCatHours(UserDetails.userId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }

                UserDetails.categories=call

                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }
    }

    private fun getUserTSNorm()
    {
        val timeWiseApi = RetrofitHelper.getInstance().create(TimeWiseApi::class.java)
        // launching a new coroutine
        GlobalScope.launch {
            try {


                val call:List<TimeSheet> = timeWiseApi.getAllTimesheets(UserDetails.userId)
                if (call.isEmpty())
                {
                    Log.d("testing","no values ")
                }
                UserDetails.ts=call
                Log.d("testing", call.toString())

            }
            catch (e:kotlin.KotlinNullPointerException)
            {
                Log.d("testing","no data")
            }

        }

    }



}
