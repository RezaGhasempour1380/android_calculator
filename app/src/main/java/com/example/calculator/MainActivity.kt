package com.example.calculator

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_calculate -> {
                    loadFragment(CalculateFragment())
                    true
                }
                R.id.navigation_history -> {
                    loadFragment(HistoryFragment())
                    true
                }
                R.id.navigation_settings -> {
                    loadFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }

        // Load the default fragment
        if (savedInstanceState == null) {
            loadFragment(CalculateFragment())
        }

        // Apply saved navigation bar color on startup
        applySavedNavigationBarColor()

        // Apply color state list to navigation bar icons
        applyNavIconColor()
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }

    fun updateNavigationBarColor(color: Int) {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setBackgroundColor(color)
        applyNavIconColor()
    }

    private fun applySavedNavigationBarColor() {
        val sharedPreferences = getSharedPreferences("AppSettings", 0)
        val savedColor = sharedPreferences.getInt("backgroundColor", Color.WHITE)
        updateNavigationBarColor(savedColor)
    }

    private fun applyNavIconColor() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val colorStateList = ContextCompat.getColorStateList(this, R.color.nav_item_color)
        bottomNavigationView.itemIconTintList = colorStateList
        bottomNavigationView.itemTextColor = colorStateList
    }
}
