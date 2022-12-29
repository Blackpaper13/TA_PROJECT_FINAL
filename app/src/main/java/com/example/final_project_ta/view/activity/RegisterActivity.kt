package com.example.final_project_ta.view.activity

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.ProgressDialog
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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import java.util.regex.Pattern

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityRegisterBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database: DatabaseReference
    private val PASSWORD_PATTERN = Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+!=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$")
    private val USERNAME_WHITE_SPACE = Pattern.compile("[A-Za-z0-9]+")
    private val PHONE_DETECT = Pattern.compile("^(^\\+62|62|^08)(\\d{3,4}-?){2}\\d{3,4}\$")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = Firebase.database.reference


        binding.registerBirth.setOnClickListener(this)
        binding.registerSubmit.setOnClickListener(this)
        binding.login.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.register_submit -> {
                //inisalisasi Item untuk registrasi pengguna
                                val email = binding.registerEmail.editableText.toString()
                                val username = binding.registerUsername.editableText.toString()
                                val fullname = binding.registerFullname.editableText.toString()
                                val birth = binding.registerBirth.editableText.toString()
                                val phone = binding.registerPhone.editableText.toString()
                                val address = binding.registerAddress.editableText.toString()
                                val password = binding.registerPassword.editableText.toString()

                                //val USERNAME_WHITE_SPACE = Pattern.compile("[A-Za-z0-9]+")
                                //deteksi username
                                if (username.isEmpty()){
                                    binding.registerUsername.error  = "Username harus diisi"
                                    binding.registerUsername.requestFocus()
                                }
                                else if (!USERNAME_WHITE_SPACE.matcher(username).matches()){
                                    binding.registerUsername.error  = "Username tidak boleh ada spasi"
                                    binding.registerUsername.requestFocus()
                                }
                                //deteksi fullname
                                else if (fullname.isEmpty()){
                                    binding.registerFullname.error = "Silakan isi Nama Lengkap anda"
                                    binding.registerFullname.requestFocus()
                                }
                                //deteksi tanggal lahir
                                else if (birth.isEmpty()){
                                    binding.registerBirth.error  = "Silakan isi tanggal lahir anda"
                                    binding.registerBirth.requestFocus()
                                }
                                //deteksi no telepon
                                else if (phone.isEmpty() && !Patterns.PHONE.matcher(phone).matches()){
                                    binding.registerPhone.error = "No Telepon Harus benar dan terisi"
                                    binding.registerPhone.requestFocus()
                                }
                                //deteksi no Telepon tidak awali dengan 08 / +62
                                /* val PHONE_DETECT = Pattern.compile("^(^\\+62|62|^08)(\\d{3,4}-?){2}\\d{3,4}\$")
                                 */
                                else if (!PHONE_DETECT.matcher(phone).matches()){
                                    binding.registerPhone.error = "No Telepon harus dimulai dengan +62, 62 atau 08"
                                    binding.registerPhone.requestFocus()
                                }
                                //deteksi alamat
                                else if (address.isEmpty()){
                                    binding.registerAddress.error = "Alamat Harus diisi"
                                    binding.registerAddress.requestFocus()
                                }
                                //Email Checking
                                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                                    binding.registerEmail.error = "Email Tidak Valid"
                                    binding.registerEmail.requestFocus()
                                }
                                else if (email.isEmpty()){
                                    binding.registerEmail.error = "Belum terisi"
                                    binding.registerEmail.requestFocus()
                                }
                                //password Checking
                                else if (password.isEmpty()){
                                    binding.registerPassword.error = "Password Tidak Terisi"
                                    binding.registerPassword.requestFocus()
                                }/*
                                val PASSWORD_PATTERN = Pattern.compile("^" +
                                        "(?=.*[0-9])" +         //at least 1 digit
                                        "(?=.*[a-z])" +         //at least 1 lower case letter
                                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                                        "(?=.*[a-zA-Z])" +      //any letter
                                        "(?=.*[@#$%^&+!=])" +    //at least 1 special character
                                        "(?=\\S+$)" +           //no white spaces
                                        ".{8,}" +               //at least 8 characters
                                        "$")
                                */
                                else if (!PASSWORD_PATTERN.matcher(password).matches()){
                                    val builder = AlertDialog.Builder(this)
                                    builder.setTitle("Perhatikan Password")
                                    builder.setMessage(R.string.alert)
                                    builder.setIcon(R.drawable.ic_baseline_add_alert_24)
                                    builder.setNeutralButton("Back"){dialogInterface, which ->
                                        showToast("Selesai baca Ketentuan")
                                    }
                                    val alertDialog = builder.create()
                                    alertDialog.setCancelable(false)
                                    alertDialog.show()

                                }
                                else if (password.length <= 8){
                                    binding.registerPassword.error = "Password Harus Lebih dari 8"
                                    binding.registerEmail.requestFocus()
                                }
                                //berhasil checking
                                else{
                                    val progressDialog = ProgressDialog(this@RegisterActivity)
                                    progressDialog.setTitle("Loading")
                                    progressDialog.setMessage("Silakan ditunggu ya")
                                    progressDialog.setCanceledOnTouchOutside(false)
                                    progressDialog.show()
                                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                                        if (task.isSuccessful){
                                            registerUserInfo(email, username, fullname, birth, phone, address, password)
                                            sendEmailVerification()
                                            showToast("Berhasil Registrasi")
                                            Intent(this@RegisterActivity, LoginActivity::class.java).also {
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                                startActivity(intent)
                                            }
                                            auth.signOut()
                                        }else {
                                            val message =task.exception!!.toString()
                                            Toast.makeText(this, "Error: $message", Toast.LENGTH_SHORT).show()
                                            FirebaseAuth.getInstance().signOut()
                                            progressDialog.dismiss()
                                        }
                                    }
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

    //masuk proses Registrasi Kedalam Database Realtime Firebase setelah FirebaseAuth
    private fun registerUserInfo(email: String, username: String, fullname: String, birth: String, phone: String, address: String, password: String)
    {
        //private lateinit var database: DatabaseReference
        //database = Firebase.database.reference
        val user = Pengguna( email, username, fullname, birth, phone, address, password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.child("Users").child(userId).setValue(user)
    }

    //
    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()?.addOnSuccessListener {
            showToast("Verifikasi Email telah terkirim .....")
        }!!.addOnFailureListener {
            showToast("Gagal Verifikasi. Silakan Periksa email anda apakah aktif")
        }

    }

    private fun showToast(s: String) {
        Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
    }


    @SuppressLint("SetTextI18n")
    private fun showDialogDate() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val datapd = DatePickerDialog(this, { _, year, month, dayOfMonth ->
            binding.registerBirth.setText("$dayOfMonth-$month-$year").toString()
        }, year, month, day)
        datapd.show()
    }

}