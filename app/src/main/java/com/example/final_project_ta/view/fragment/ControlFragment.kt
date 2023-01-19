package com.example.final_project_ta.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.final_project_ta.R
import com.example.final_project_ta.databinding.FragmentControlBinding
import com.example.final_project_ta.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ControlFragment : Fragment() {

    private var _binding: FragmentControlBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference

    val on_status = "ON"
    val off_status = "OFF"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentControlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()

        binding.tb1.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val ganti_on =  mapOf("statusPintu" to on_status)
                val userId = auth.currentUser!!.uid
                database.child("Users").child(userId).child("ujicoba").updateChildren(ganti_on).addOnSuccessListener {
                    Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                }
            }else {
                val ganti_off =  mapOf("statusPintu" to off_status)
                val userId = auth.currentUser!!.uid
                database.child("Users").child(userId).child("ujicoba").updateChildren(ganti_off).addOnSuccessListener {
                    Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tb2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val ganti_on =  mapOf("relay2" to on_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_on).addOnSuccessListener {
                    Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                }
            }else {
                val ganti_off =  mapOf("relay2" to off_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_off).addOnSuccessListener {
                    Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tb3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val ganti_on = mapOf("relay1" to on_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_on).addOnSuccessListener {
                    Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                }
            }else {
                val ganti_off =  mapOf("relay1" to off_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_off).addOnSuccessListener {
                    Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tb4.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val ganti_on = mapOf("relay3" to on_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_on).addOnSuccessListener {
                    Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                }
            }else {
                val ganti_off =  mapOf("relay3" to off_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_off).addOnSuccessListener {
                    Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.tb5.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val ganti_on = mapOf("relay4" to on_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_on).addOnSuccessListener {
                    Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                }
            }else {
                val ganti_off =  mapOf("relay4" to off_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_off).addOnSuccessListener {
                    Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                }
            }
        }




    }


}