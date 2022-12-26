@file:Suppress("DEPRECATION")

package com.example.final_project_ta.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.final_project_ta.databinding.ActivityEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding

    companion object{
        private const val CAMERA_REQUEST_CODE = 100
    }

    private lateinit var imageUri: Uri
    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        if (user != null) {
            if (user.photoUrl == null) {
                Picasso.get().load("https://picsum.photos/seed/picsum/200/300").into(binding.imageChangeProfile)
            }else {
                Picasso.get().load(user.photoUrl).into(binding.imageChangeProfile)
            }
            database = FirebaseDatabase.getInstance().getReference("Users")
            getData()
        }

        //simpan perubahan Profil
        binding.buttonSaveChangeProfil.setOnClickListener {
            val nama_lengkap = binding.profileNama.editableText.toString()
            val username = binding.profileUsername.editableText.toString()
            val alamat = binding.profileAlamat.editableText.toString()
            val no_hp = binding.profilePhone.editableText.toString()

            uploadDataProfil(nama_lengkap, username, alamat, no_hp)

            val photo = when {
                ::imageUri.isInitialized -> imageUri
                user?.photoUrl == null -> Uri.parse("https://picsum.photos/id/237/200/300")
                else -> user.photoUrl
            }

            UserProfileChangeRequest.Builder()
                .setPhotoUri(photo)
                .build().also {
                    user?.updateProfile(it)?.addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(this, "Berhasil Update Profil", Toast.LENGTH_SHORT).show()
                        }else {
                            Toast.makeText(this, "ERROR ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)


        }

        //ganti gambar Profil
        binding.buttonChangeProfil.setOnClickListener {
                openCameraForImage()
        }
    }

    //proses upload profil
    @SuppressLint("SuspiciousIndentation")
    private fun uploadDataProfil(namaLengkap: String, username: String, alamat: String, noHp: String) {
        val User =  mapOf(
                "fullname" to namaLengkap,
                "username" to username,
                "address" to alamat,
                "phone" to noHp )
            val userId = auth.currentUser!!.uid
            database.child(userId).updateChildren(User).addOnSuccessListener {
                showToast("berhasil Update data")
            }.addOnFailureListener {
                showToast("Gagal Update Data anda")
            }

    }

    private fun getData() {
        val userId = auth.currentUser!!.uid
        database.child(userId).get().addOnSuccessListener {
            val fullname = it.child("fullname").value.toString()
            val username = it.child("username").value.toString()
            val alamat = it.child("address").value.toString()
            val telepon = it.child("phone").value.toString()

            binding.profileNama.setText(fullname)
            binding.profileUsername.setText(username)
            binding.profileAlamat.setText(alamat)
            binding.profilePhone.setText(telepon)
        }.addOnFailureListener {
            showToast("Failed Load Data")
        }
    }


    private fun openCameraForImage() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
           this.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, CAMERA_REQUEST_CODE)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadImage(imgBitmap)
        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
        val output = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100,output)
        val image = output.toByteArray()

        ref.putBytes(image)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener {
                        it.result?.let {
                            imageUri = it
                            binding.imageChangeProfile.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }

    }


//    private val galleryActivityResultLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ){result ->
//        if (result.resultCode == Activity.RESULT_OK){
//            val data = result.data
//
//            imageUri = data!!.data
//            Log.d(TAG,"galleryActivityResultLauncher: imageUri: $imageUri")
//
////            binding.imageChangeProfile.setImageURI(imageUri)
//
//        }else {
//            showToast("Dibatalkan ..............")
//        }
//    }



    private fun showToast(message: String) {
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show()
    }


}