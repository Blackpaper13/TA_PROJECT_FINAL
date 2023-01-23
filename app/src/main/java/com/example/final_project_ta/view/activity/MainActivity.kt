package com.example.final_project_ta.view.activity

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.example.final_project_ta.R
import com.example.final_project_ta.databinding.ActivityMainBinding
import com.example.final_project_ta.view.fragment.ControlFragment
import com.example.final_project_ta.view.fragment.HomeFragment
import com.example.final_project_ta.view.fragment.SettingsFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference

    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelName"
    val NOTIF_ID = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(HomeFragment())
        binding.bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.home -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.control -> {
                    loadFragment(ControlFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.settings -> {
                    loadFragment(SettingsFragment())
                    return@setOnNavigationItemReselectedListener
                }
        }
    }

        createNotification()
        val intent= Intent(this,MainActivity::class.java)
        val pendingIntent = TaskStackBuilder.create(this).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notif = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("WARNING!!!")
            .setContentText("PINTU ANDA TERBUKA, SILAKAN CHECK BAGIAN KAMAR ANDA SEGERA")
            .setSmallIcon(R.drawable.baseline_warning_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()
        val notif1 = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("WARNING!!!")
            .setContentText("PINTU ANDA SUDAH TERKUNCI DENGAN BENAR")
            .setSmallIcon(R.drawable.baseline_warning_24)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()


        val notifManger = NotificationManagerCompat.from(this)

        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser!!.uid

        database.child("Users").child(userId).get().addOnSuccessListener {
            val status_reed = it.child("ujicoba/pintuKondisi").value.toString()
            if (status_reed == "TERBUKA"){
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@addOnSuccessListener
                }
                notifManger.notify(NOTIF_ID, notif)
            } else if (status_reed == "TERTUTUP"){
                if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return@addOnSuccessListener
                }
                notifManger.notify(NOTIF_ID, notif1)
            }
        }

}

    private fun createNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH).apply {
                lightColor = Color.BLUE
                enableLights(true)
            }
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}
