package com.example.a7minutesworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.a7minutesworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    lateinit var drawerLayout: DrawerLayout
    lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
//  Drawer Functionality
        drawerLayout = binding!!.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(this,drawerLayout, R.string.nav_open,R.string.nav_close)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

       binding?.flStart?.setOnClickListener{
//            Toast.makeText(this@MainActivity,

           val intent  = Intent(this,ExerciseActivity::class.java)
           startActivity(intent)
        }
        binding?.flBMI?.setOnClickListener{
            val intent = Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }
        binding?.history?.setOnClickListener {
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }
        binding?.locateGym?.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java )
            startActivity(intent)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        }
        else super.onOptionsItemSelected(item)

    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}