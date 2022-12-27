package com.example.final_project_ta.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.final_project_ta.R
import com.example.final_project_ta.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.daftar.setOnClickListener(this)
        binding.loginHome.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.login_home -> {
                val username = binding.username.editableText.toString().trim()
                val password = binding.pasword.editableText.toString().trim()

                if (username.isEmpty()){
                    binding.username.error="Email Kosong, Silakan Isi"
                    binding.username.requestFocus()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()){
                    binding.username.error="Email tidak valid"
                    binding.username.requestFocus()
                }
                else if (password.isEmpty() || password.length <= 6){
                    binding.pasword.error = "Password Kosong atau Password harus lebih dari 6"
                    binding.pasword.requestFocus()
                }
                else {
                    loginUser(username, password)
                }
            }

            R.id.daftar -> {
                val intent = Intent(applicationContext, RegisterActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun loginUser(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val firebaseEmailVerif = auth.currentUser
                    if (firebaseEmailVerif!!.isEmailVerified){
                        Intent(this@LoginActivity, MainActivity::class.java).also { it ->
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    }else {
                            val user = auth.currentUser
                            user?.sendEmailVerification()?.addOnSuccessListener {
                                showToast("Verifikasi Email telah terkirim .....")
                            }!!.addOnFailureListener {
                                showToast("Gagal Verifikasi. Silakan Periksa email anda apakah aktif")
                            }
                        Toast.makeText(this, "Email Belum Di verifikasi silalkan check email anda", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Gagal Login karena Email atau Password salah. Silakan Dicoba ulang", Toast.LENGTH_SHORT).show()
                }
            }
    }


    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            if (auth.currentUser!!.isEmailVerified) {
                Intent(this@LoginActivity, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }else {
                auth.signOut()
            }
        }
    }
    private fun showToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }
}