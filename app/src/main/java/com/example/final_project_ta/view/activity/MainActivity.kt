package com.example.final_project_ta.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.final_project_ta.R
import com.example.final_project_ta.databinding.ActivityMainBinding
import com.example.final_project_ta.view.fragment.ControlFragment
import com.example.final_project_ta.view.fragment.HomeFragment
import com.example.final_project_ta.view.fragment.SettingsFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(HomeFragment())
        binding.bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.control -> {
                    loadFragment(ControlFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.settings -> {
                    loadFragment(SettingsFragment())
                    return@setOnNavigationItemReselectedListener
                }
        }
    }

}

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
