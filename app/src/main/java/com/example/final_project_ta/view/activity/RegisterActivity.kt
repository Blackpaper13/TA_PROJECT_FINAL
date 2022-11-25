package com.example.final_project_ta.view.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import com.example.final_project_ta.R
import com.example.final_project_ta.databinding.ActivityRegisterBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.Year
import java.time.YearMonth
import java.util.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.registerBirth.setOnClickListener(this)
        binding.registerSubmit.setOnClickListener(this)
        binding.login.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.register_submit -> {
                if(binding.registerEmail.editableText.toString().isEmpty() && binding.registerUsername.editableText.toString().isEmpty()  &&
                        binding.registerFullname.editableText.toString().isEmpty()  && binding.registerBirth.editableText.toString().isEmpty()
                    && binding.registerPhone.editableText.toString().isEmpty()
                    && binding.registerAddress.editableText.toString().isEmpty()  && binding.registerPassword.editableText.toString().isEmpty() ){
                    Toast.makeText(this, "Perlu diisi terlebih dahulu",Toast.LENGTH_SHORT).show()
                }else {
                    val intent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(intent)
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

    private fun showDialogDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datapd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            binding.registerBirth.setText("$dayOfMonth-$month-$year").toString()
        }, year, month, day)
        datapd.show()
    }

}