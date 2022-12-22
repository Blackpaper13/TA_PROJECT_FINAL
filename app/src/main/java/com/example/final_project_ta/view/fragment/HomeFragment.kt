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


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference
    private var list = mutableListOf<Pengguna>()


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

        auth = FirebaseAuth.getInstance()
        val user = auth.currentUser

        database = FirebaseDatabase.getInstance().getReference("Users")
        getData()
    }

    private fun getData() {
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    val id = ds.key
                    val username = ds.child("username").value.toString()

                    binding.textShowUsername.text = username
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Failed show Username", Toast.LENGTH_SHORT).show()
            }

        })
    }


}