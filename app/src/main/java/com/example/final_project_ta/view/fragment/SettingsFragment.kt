package com.example.final_project_ta.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.final_project_ta.databinding.FragmentSettingsBinding
import com.example.final_project_ta.view.activity.EditProfileActivity
import com.example.final_project_ta.view.activity.LoginActivity
import com.example.final_project_ta.model.Pengguna
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference
    private var list = mutableListOf<Pengguna>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root

        readData()
    }

    private fun readData() {
        val username = binding.usernameSettings.text.toString()
        database = FirebaseDatabase.getInstance().getReference("Users")
        database.child("username").get().addOnSuccessListener {
            if (it.exists()) {
                val username = it.child("username").value.toString()
                val telepon = it.child("phone").value.toString()
                val alamat = it.child("address").value.toString()

                binding.usernameSettings.text = username

            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser


        binding.keluarSettings.setOnClickListener {
            auth.signOut()
            Intent(this@SettingsFragment.requireContext(), LoginActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }

        }

        binding.buttonEditSettings.setOnClickListener {
            val intent =
                Intent(this@SettingsFragment.requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}