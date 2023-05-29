package com.example.timewisefrontend.screens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.timewisefrontend.R
import com.example.timewisefrontend.databinding.ActivityMainMenuBinding
import android.view.Menu
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.example.timewisefrontend.fragments.*

class MainMenuActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainMenuBinding
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar =  findViewById(R.id.toolbar)
        //setSupportActionBar(toolbar)

        binding.apply {
            toggle =
                ActionBarDrawerToggle(this@MainMenuActivity, drawerLayout,toolbar, R.string.open, R.string.close)
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            navView.setCheckedItem(R.id.nav_dashboard)
            navView.setNavigationItemSelectedListener{

                it.isChecked=true
                when(it.itemId) {
                    R.id.nav_profile -> {loadFrag(ProfileFragment())
                        binding.drawerLayout.closeDrawers() }
                    R.id.nav_dashboard -> { loadFrag(DashboardFragment())
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_settings ->{ loadFrag(SettingsFragment())
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_timesheet -> {loadFrag(TimeSheetFragment())
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_category -> {loadFrag(CategoryFragment())
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_stats -> {loadFrag(StatsFragament())
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_logout -> {// TODO: removed user data from memory
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






