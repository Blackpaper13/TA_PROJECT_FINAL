package com.example.final_project_ta.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.final_project_ta.databinding.FragmentSettingsBinding
import com.example.final_project_ta.view.activity.EditProfileActivity
import com.example.final_project_ta.view.activity.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class SettingsFragment : Fragment() {

    private lateinit var _binding: FragmentSettingsBinding
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding
    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid

        if (auth.currentUser!!.photoUrl == null) {
            Picasso.get().load("https://picsum.photos/seed/picsum/200/300")
                .into(binding.imageProfile)
        } else {
            Picasso.get().load(auth.currentUser!!.photoUrl).into(binding.imageProfile)
        }
        database.child("Users").child(userId).get().addOnSuccessListener {
            val username = it.child("username").value.toString()
            val telepon = it.child("phone").value.toString()
            val alamat = it.child("address").value.toString()

            binding.usernameSettings.text = username
            binding.noHpSettings.text = telepon
            binding.alamatSettings.text = alamat

        }.addOnFailureListener {
            showToast("failed Load Username")
        }


        binding.keluarSettings.setOnClickListener {
            auth.signOut()
            val intent = Intent(context, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            activity?.run {
                supportFragmentManager.beginTransaction().remove(this@SettingsFragment)
                    .commitAllowingStateLoss()
            }
        }

        binding.buttonEditSettings.setOnClickListener {
            val intent = Intent(this@SettingsFragment.requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(s: String) {
        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
    }




}