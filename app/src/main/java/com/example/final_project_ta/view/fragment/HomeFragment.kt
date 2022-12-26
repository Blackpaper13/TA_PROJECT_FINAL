package com.example.final_project_ta.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.final_project_ta.R
import com.example.final_project_ta.databinding.FragmentHomeBinding
import com.example.final_project_ta.model.Pengguna
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
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

            binding.textShowUsername.text = username
        }.addOnFailureListener {
            showToast("failed Load Username")
        }


    }

    private fun showToast(s: String) {
        Toast.makeText(activity, s,Toast.LENGTH_SHORT).show()
    }


}