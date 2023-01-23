package com.example.final_project_ta.view.fragment

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.telecom.Connection.RttTextStream
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.final_project_ta.databinding.FragmentHomeBinding
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

    @SuppressLint("SetTextI18n")
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
            val status_watt = it.child("BiayaListrik/watt").value.toString()
            val status_watt_rupiah = it.child("BiayaListrik/kwh").value.toString()
            val status_pintu = it.child("ujicoba/statusPintu").value.toString()
            val perhitungan = status_watt_rupiah.toFloat() * 1445
            val final = perhitungan.toString()

            binding.textShowUsername.text = username
            if (status_pintu == "BUKA"){
                binding.statusPintu.text = "KUNCI"
            } else if (status_pintu == "KUNCI") {
                binding.statusPintu.text = "BUKA"
            }
            binding.statusWatt.text = "$status_watt watt"
            binding.statusWattRupiah.text = "Rp $final"
        }.addOnFailureListener {
            showToast("failed Load Username")
        }

        database.child("test").get().addOnSuccessListener {
            val status_suhu = it.child("dht/temperature").value.toString()
            val status_water_tank = it.child("ujicoba/tinggiAir").value.toString()
            val status_jemuran = it.child("rain/statusJemuran").value.toString().toInt()
            val nilai_air = status_water_tank.toFloat()

            //pengkondisian nilai_air untuk persentase ketinggian air.
            if (nilai_air > 22.831) {
                binding.statusWaterTank.text = "Air Kosong"
            } else if ( nilai_air > 13.67  && nilai_air <= 18.26 ) {
                binding.statusWaterTank.text = "Terisi 20 %"
            }else if ( nilai_air > 9.13 && nilai_air <= 13.67) {
                binding.statusWaterTank.text = "terisi 40 %"
            }else if (nilai_air > 4.57 && nilai_air <= 9.13 ) {
                binding.statusWaterTank.text = "Terisi 60%"
            }else if (nilai_air > 2.7 && nilai_air <=4.57){
                binding.statusWaterTank.text = "Terisi 80%"
            } else if (nilai_air > 2 && nilai_air <= 2.7){
                showToast("Sudah hampir Penuh, silakan matikan Pompa Air")
            } else if (nilai_air <=2 ){
                binding.statusWaterTank.text = "Penuh"
                val matikan_relay_pompa = "OFF"
                matikanRelayPompaAir(matikan_relay_pompa)
                showToast("Sudah Penuh, akan langsung dimatikan pompanya, silakan matikan button Pompa Air")
            }else {
                showToast("nilai error")
            }

            if (status_jemuran >= 200) {
                    binding.statusJemuran.text = "Pakaian di Jemur"
            } else if (status_jemuran < 200) {
                binding.statusJemuran.text = "Pakaian ditepi"
            }

            binding.statusSuhu.text = "$status_suhu \u2103"
        }.addOnFailureListener {
            showToast("failed Load Status Sensor")
        }


    }

    private fun matikanRelayPompaAir(matikan_relay_pompa: String) {
        val User =  mapOf("relay3" to matikan_relay_pompa)
        database.child("test/ujicoba/relay").updateChildren(User)
    }

    private fun showToast(s: String) {
        Toast.makeText(activity, s,Toast.LENGTH_SHORT).show()
    }


}