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



//public class MainActivity extends AppCompatActivity {
//    private DrawerLayout mDrawer;
//    private Toolbar toolbar;
//    private NavigationView nvDrawer;
//
//    // Make sure to be using androidx.appcompat.app.ActionBarDrawerToggle version.
//
//    private ActionBarDrawerToggle drawerToggle;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(.Rlayout.activity_main);
//
//        // Set a Toolbar to replace the ActionBar.
//
//        toolbar = (Toolbar) findViewById(.id.Rtoolbar);
//        setSupportActionBar(toolbar);
//
//        // This will display an Up icon (<-), we will replace it with hamburger later
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        // Find our drawer view
//
//        mDrawer = (DrawerLayout) findViewById(.id.Rdrawer_layout);
//        setupDrawerContent(nvDrawer);
//    }
//
//    private void setupDrawerContent(NavigationView navigationView) {
//        navigationView.setNavigationItemSelectedListener(
//            new NavigationView.OnNavigationItemSelectedListener() {
//                @Override
//                public boolean onNavigationItemSelected(MenuItem menuItem) {
//                    selectDrawerItem(menuItem);
//                    return true;
//                }
//            });
//    }
//
//    public void selectDrawerItem(MenuItem menuItem) {
//        // Create a new fragment and specify the fragment to show based on nav item clicked
//
//        Fragment fragment = null;
//        Class fragmentClass;
//        switch(menuItem.getItemId()) {
//            case .id.Rnav_first_fragment:
//            fragmentClass = FirstFragment.class;
//            break;
//            case .id.Rnav_second_fragment:
//            fragmentClass = SecondFragment.class;
//            break;
//            case .id.Rnav_third_fragment:
//            fragmentClass = ThirdFragment.class;
//            break;
//            default:
//            fragmentClass = FirstFragment.class;
//        }
//
//        try {
//            fragment = (Fragment) fragmentClass.newInstance();
//        } catch (Exception ) {
//            .eeprintStackTrace();
//        }
//
//        // Insert the fragment by replacing any existing fragment
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().replace(.id.RflContent, fragment).commit();
//
//        // Highlight the selected item has been done by NavigationView
//
//        menuItem.setChecked(true);
//        // Set action bar title
//
//        setTitle(menuItem.getTitle());
//        // Close the navigation drawer
//
//        mDrawer.closeDrawers();
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // The action bar home/up action should open or close the drawer.
//
//        switch (item.getItemId()) {
//            case android..id.Rhome:
//            mDrawer.openDrawer(GravityCompat.START);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//}



