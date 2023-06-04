package com.example.timewisefrontend.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.timewisefrontend.R
import com.example.timewisefrontend.databinding.ActivityMainMenuBinding
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.timewisefrontend.fragments.*
import com.example.timewisefrontend.models.UserDetails
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainMenuBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(
            this@MainMenuActivity,
            "Logged in Successfully!",
            Toast.LENGTH_SHORT
        ).show()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        actionBar?.hide()
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar =  findViewById(R.id.toolbar)
        toolbar.title=("Dashboard")
        //setSupportActionBar(toolbar)

        //TODO: declare global user id variable, look into companion objects


        binding.apply {
            toggle =
                ActionBarDrawerToggle(this@MainMenuActivity, drawerLayout,toolbar, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            val header:View=navView.getHeaderView(0)
            val email:TextView=header.findViewById(R.id.emailPlaceHolder)
            email.text=UserDetails.email
            val name:TextView=header.findViewById(R.id.usernamePlaceHolder)
            name.text=UserDetails.name
            navView.setCheckedItem(R.id.nav_dashboard)
            navView.setNavigationItemSelectedListener{


                it.isChecked=true
                when(it.itemId) {
                    R.id.nav_profile -> {loadFrag(ProfileFragment())
                        toolbar.title="Profile"
                        binding.drawerLayout.closeDrawers() }
                    R.id.nav_dashboard -> { loadFrag(DashboardFragment())
                        toolbar.title=("Dashboard")
                        binding.drawerLayout.closeDrawers()}
//                    R.id.nav_settings ->{ loadFrag(SettingsFragment())
//                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_timesheet -> {loadFrag(TimeSheetFragment())
                        toolbar.title=("TimeSheet")
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_category -> {loadFrag(CategoryFragment())
                        toolbar.title=("Category")
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_stats -> {loadFrag(StatsFragament())
                        toolbar.title=("Statistics")
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_logout -> {// TODO: removed user data from memory
                                        FirebaseAuth.getInstance().signOut()
                                        UserDetails.email=""
                                        UserDetails.name=""
                                        UserDetails.userId=""
                                        UserDetails.categories= emptyList()
                                        val intent = Intent(this@MainMenuActivity, StartActivity ::class.java)
                                        startActivity(intent)}
                }
                true
            }

        }
        loadFrag(DashboardFragment())



    }

    fun loadFrag(fragment:Fragment)
    {
        val fragmentManager:FragmentManager  =supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flContent,fragment).commit()
    }



    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.nav_menu , menu)
        return true
    }
}






