package com.example.final_project_ta.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.core.view.isEmpty
import com.example.final_project_ta.R
import com.example.final_project_ta.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_register.*

class ResetPasswordActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResetPasswordBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnResetPassword.setOnClickListener{
            val email_send_resetPassword = binding.resetPassword.editableText.toString().trim()
            if (email_send_resetPassword.isEmpty()) {
                binding.resetPassword.error = "Email harus diisi dengan benar"
                binding.resetPassword.requestFocus()
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email_send_resetPassword).matches()){
                binding.resetPassword.error = "Email harus valid dan terdaftar"
                binding.resetPassword.requestFocus()
            }
            else {
                auth.sendPasswordResetEmail(email_send_resetPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showToast("berhasil Kirim link reset Password, silakan cek email anda")
                        Intent(this@ResetPasswordActivity, LoginActivity::class.java).also { it ->
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    }else {
                        showToast("${task.exception?.message}")
                    }
                }
            }
        }


    }

    private fun showToast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}