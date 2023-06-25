package com.example.timewisefrontend.screens

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.timewisefrontend.R
import com.example.timewisefrontend.databinding.ActivityMainMenuBinding
import android.view.Menu
import android.view.View
import android.view.Window.FEATURE_NO_TITLE
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.timewisefrontend.fragments.*
import com.example.timewisefrontend.models.UserDetails
import com.google.android.material.color.MaterialColors
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth

class MainMenuActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainMenuBinding
    lateinit var toggle: ActionBarDrawerToggle
    private var savedState: Bundle? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        actionBar?.hide()
        binding = ActivityMainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: Toolbar =  findViewById(R.id.toolbar)
        toolbar.title=("Dashboard")

        val nightModeSwitch = binding.navView.menu.findItem(R.id.night_mode).actionView as MaterialSwitch
        nightModeSwitch.thumbIconDrawable=getDrawable(R.drawable.baseline_sunny_24)
        nightModeSwitch.text="Day Mode"
        nightModeSwitch.textSize=22f
        nightModeSwitch.minHeight=40
        val color = MaterialColors.getColor(this,
            com.google.android.material.R.attr.colorPrimary, Color.BLACK)
        nightModeSwitch.trackTintList= ColorStateList.valueOf(color)
       val colorP = MaterialColors.getColor(this,
           com.google.android.material.R.attr.colorPrimaryVariant, Color.BLACK)
        nightModeSwitch.thumbTintList= ColorStateList.valueOf(colorP)


        nightModeSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
             if (isChecked)
             {
                nightModeSwitch.thumbIconDrawable=getDrawable(R.drawable.baseline_night_24)
                 nightModeSwitch.text="Night Mode"
                 val color = MaterialColors.getColor(this,
                     com.google.android.material.R.attr.colorPrimary, Color.BLACK)
                 nightModeSwitch.trackTintList= ColorStateList.valueOf(color)

                 val colorP = MaterialColors.getColor(this,
                 com.google.android.material.R.attr.colorOnPrimary, Color.BLACK)
                nightModeSwitch.thumbTintList= ColorStateList.valueOf(colorP)
                 AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                 delegate.applyDayNight()
            }

             else
            {
                 nightModeSwitch.thumbIconDrawable=getDrawable(R.drawable.baseline_sunny_24)
                 nightModeSwitch.text="Day Mode"
                 val color = MaterialColors.getColor(this,
                     com.google.android.material.R.attr.colorPrimary, Color.BLACK)
                 nightModeSwitch.trackTintList= ColorStateList.valueOf(color)
                 val colorP = MaterialColors.getColor(this,
                     com.google.android.material.R.attr.colorPrimaryVariant, Color.BLACK)
                 nightModeSwitch.thumbTintList= ColorStateList.valueOf(colorP)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                delegate.applyDayNight()
            }

            Log.d("testing","onCheckedChanged: $isChecked")
        }
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
                    R.id.nav_timesheet -> {loadFrag(TimeSheetFragment())
                        toolbar.title=("TimeSheet")
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_category -> {loadFrag(CategoryFragment())
                        toolbar.title=("Category")
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_stats -> {loadFrag(StatsFragment())
                        toolbar.title=("Statistics")
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_graph -> {loadFrag(GraphFragment())
                        toolbar.title=("Graphs")
                        binding.drawerLayout.closeDrawers()}
                    R.id.nav_calendar -> {loadFrag(CalendarFragment())
                        toolbar.title=("Calendar")
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

    private fun loadFrag(fragment:Fragment)
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






