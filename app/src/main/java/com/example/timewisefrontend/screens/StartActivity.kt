package com.example.timewisefrontend.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.timewisefrontend.R

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
    }
    fun next(view: View)
    {
        val intent= Intent(this,MainMenuActivity ::class.java)
        startActivity(intent)
    }
}