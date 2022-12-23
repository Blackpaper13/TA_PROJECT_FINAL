package com.example.final_project_ta.view.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.final_project_ta.R
import com.example.final_project_ta.databinding.ActivityRegisterBinding
import com.example.final_project_ta.model.Pengguna
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*

@Suppress("NAME_SHADOWING")
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.registerBirth.setOnClickListener(this)
        binding.registerSubmit.setOnClickListener(this)
        binding.login.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.register_submit -> {
                //inisalisasi Item untuk registrasi pengguna
                val email = binding.registerEmail.editableText.toString().trim()
                val username = binding.registerUsername.editableText.toString().trim()
                val fullname = binding.registerFullname.editableText.toString().trim()
                val birth = binding.registerBirth.editableText.toString().trim()
                val phone = binding.registerPhone.editableText.toString().trim()
                val address = binding.registerAddress.editableText.toString().trim()
                val password = binding.registerPassword.editableText.toString().trim()

                if(email.isEmpty() || username.trim().isEmpty()  || fullname.isEmpty()  || birth.isEmpty()
                    || phone.isEmpty() || address.isEmpty()  || password.isEmpty() ){
                    Toast.makeText(this, "Registrasi Harus Lengkap dahulu",Toast.LENGTH_SHORT).show()
                } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.registerEmail.editableText.toString().trim()).matches()){
                    binding.registerEmail.error = "Email isn't Valid"
                    binding.registerEmail.requestFocus()
                }
                else if (binding.registerPassword.editableText.toString().trim().isEmpty() || binding.registerPassword.editableText.toString().trim().length < 8){
                    binding.registerPassword.error = "Password Harus Terisi atau Password Kurang dari 8 karakter"
                    binding.registerPassword.requestFocus()
                }
                else {
                        database = FirebaseDatabase.getInstance().getReference("Users")
                        val User = Pengguna(email, username, fullname, birth, phone, address, password)
                        database.child(username).setValue(User).addOnCompleteListener {
                                registrasiUser(email,password)
                            }

//                    val intent = Intent(applicationContext, LoginActivity::class.java)
//                    startActivity(intent)
                }
            }
            R.id.login -> {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            }

            R.id.register_birth -> {
                showDialogDate()
            }

        }
    }


    private fun registrasiUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.registerEmail.text?.clear()
                    binding.registerUsername.text?.clear()
                    binding.registerFullname.text?.clear()
                    binding.registerBirth.text?.clear()
                    binding.registerPhone.text?.clear()
                    binding.registerAddress.text?.clear()
                    binding.registerPassword.text?.clear()
                    Toast.makeText(this, "Success Registration", Toast.LENGTH_SHORT).show()
                    sendEmailVerification()
                    Intent(this@RegisterActivity, LoginActivity::class.java).also { it ->
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                }else {
                    Toast.makeText(this, "Register Gagal Silakan Dicoba ulang", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnSuccessListener {
            Toast.makeText(this@RegisterActivity, "Verifikasi Email telah terkirim .....", Toast.LENGTH_SHORT).show()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun showDialogDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datapd = DatePickerDialog(this, { view, year, month, dayOfMonth ->
            binding.registerBirth.setText("$dayOfMonth-$month-$year").toString()
        }, year, month, day)
        datapd.show()
    }

}