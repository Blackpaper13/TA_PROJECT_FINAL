package com.example.final_project_ta.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.final_project_ta.R
import com.example.final_project_ta.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.daftar.setOnClickListener(this)
        binding.loginHome.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.login_home -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
            }

            R.id.daftar -> {
                val intent = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }
}