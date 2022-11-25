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


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.keluarSettings.setOnClickListener {
            val intent = Intent(this@SettingsFragment.requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.buttonEditSettings.setOnClickListener {
            val intent =
                Intent(this@SettingsFragment.requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }




}