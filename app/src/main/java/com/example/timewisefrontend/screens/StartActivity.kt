package com.example.timewisefrontend.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.timewisefrontend.R

class    StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()

        supportActionBar?.hide()

        Handler().postDelayed({
            val intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}