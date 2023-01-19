package com.example.final_project_ta.view.fragment

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
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

    @SuppressLint("CommitPrefEdits")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()


        val my_pref = "swtich_prefs"
        val SWITCH_STATUS = "switch_status"
        val SWITCH_STATUS1 = "switch_status1"
        val SWITCH_STATUS2 = "switch_status2"
        val SWITCH_STATUS3 = "switch_status3"
        val SWITCH_STATUS4 = "switch_status4"


        val myPreferences = this.activity?.getSharedPreferences(my_pref, MODE_PRIVATE)
        val myEditor = myPreferences?.edit()

        var switch_status  = false
        var switch_status1  = false
        var switch_status2  = false
        var switch_status3  = false
        var switch_status4  = false

        if (myPreferences != null) {
            switch_status= myPreferences.getBoolean(SWITCH_STATUS, false)
            switch_status1= myPreferences.getBoolean(SWITCH_STATUS1, false)
            switch_status2= myPreferences.getBoolean(SWITCH_STATUS2, false)
            switch_status3= myPreferences.getBoolean(SWITCH_STATUS3, false)
            switch_status4= myPreferences.getBoolean(SWITCH_STATUS4, false)

        }

        binding.tb1.isChecked = switch_status
        binding.tb2.isChecked = switch_status1
        binding.tb3.isChecked = switch_status2
        binding.tb4.isChecked = switch_status3
        binding.tb5.isChecked = switch_status4


        binding.tb1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val ganti_on =  mapOf("statusPintu" to on_status)
                val userId = auth.currentUser!!.uid
                database.child("Users").child(userId).child("ujicoba").updateChildren(ganti_on).addOnSuccessListener {
                    Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                }
                myEditor?.putBoolean(SWITCH_STATUS, true);
                myEditor?.apply()
                binding.tb1.isChecked = true
            }else {
                val ganti_off =  mapOf("statusPintu" to off_status)
                val userId = auth.currentUser!!.uid
                database.child("Users").child(userId).child("ujicoba").updateChildren(ganti_off).addOnSuccessListener {
                    Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                }
                myEditor?.putBoolean(SWITCH_STATUS, false);
                myEditor?.apply()
                binding.tb1.isChecked = false
            }
        }

        binding.tb2.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val ganti_on =  mapOf("relay2" to on_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_on).addOnSuccessListener {
                    Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                }
                myEditor?.putBoolean(SWITCH_STATUS1, true);
                myEditor?.apply()
                binding.tb2.isChecked = true
            }else {
                val ganti_off =  mapOf("relay2" to off_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_off).addOnSuccessListener {
                    Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                }
                myEditor?.putBoolean(SWITCH_STATUS1, false);
                myEditor?.apply()
                binding.tb2.isChecked = false
            }

        }

        binding.tb3.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val ganti_on = mapOf("relay1" to on_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_on).addOnSuccessListener {
                    Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                }
                myEditor?.putBoolean(SWITCH_STATUS2, true);
                myEditor?.apply()
                binding.tb3.isChecked = true
            }else {
                val ganti_off =  mapOf("relay1" to off_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_off).addOnSuccessListener {
                    Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                }
                myEditor?.putBoolean(SWITCH_STATUS2, false);
                myEditor?.apply()
                binding.tb3.isChecked = false
            }
        }

        binding.tb4.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                val ganti_on = mapOf("relay3" to on_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_on).addOnSuccessListener {
                    Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                }
                myEditor?.putBoolean(SWITCH_STATUS3, true);
                myEditor?.apply()
                binding.tb4.isChecked = true
            }else {
                val ganti_off =  mapOf("relay3" to off_status)
                database.child("test").child("ujicoba/relay").updateChildren(ganti_off).addOnSuccessListener {
                    Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                }
                myEditor?.putBoolean(SWITCH_STATUS3, false);
                myEditor?.apply()
                binding.tb4.isChecked = false
            }
        }

        binding.tb5.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    val ganti_on = mapOf("relay4" to on_status)
                    database.child("test").child("ujicoba/relay").updateChildren(ganti_on)
                        .addOnSuccessListener {
                            Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
                        }
                    myEditor?.putBoolean(SWITCH_STATUS4, true);
                    myEditor?.apply()
                    binding.tb5.isChecked = true

                } else {
                    val ganti_off = mapOf("relay4" to off_status)
                    database.child("test").child("ujicoba/relay").updateChildren(ganti_off)
                        .addOnSuccessListener {
                            Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
                        }
                    myEditor?.putBoolean(SWITCH_STATUS4, false);
                    myEditor?.apply()
                    binding.tb5.isChecked = false
                }

        }




    }


}