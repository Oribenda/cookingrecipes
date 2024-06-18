package com.example.cookingrecipes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsFragment : Fragment() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        sharedPreferences = requireActivity().getSharedPreferences("app_settings", Context.MODE_PRIVATE)

        // Handle notification switch
        val switchNotifications: SwitchMaterial = view.findViewById(R.id.switch_notifications)
        switchNotifications.isChecked = sharedPreferences.getBoolean("enable_notifications", false)
        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("enable_notifications", isChecked).apply()
        }

        // Handle dark mode switch
        val switchDarkMode: SwitchMaterial = view.findViewById(R.id.switch_dark_mode)
        switchDarkMode.isChecked = sharedPreferences.getBoolean("dark_mode", false)
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
            applyDarkMode(isChecked)
        }

        // Handle language spinner
        val spinnerLanguage: Spinner = view.findViewById(R.id.spinner_language)
        val languages = arrayOf("English", "Hebrew")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguage.adapter = adapter

        val currentLanguage = sharedPreferences.getString("language", "en")
        spinnerLanguage.setSelection(if (currentLanguage == "he") 1 else 0)
        spinnerLanguage.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedLanguage = if (position == 1) "he" else "en"
                sharedPreferences.edit().putString("language", selectedLanguage).apply()
                applyLanguage(selectedLanguage)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Handle play button
        val playButton: Button = view.findViewById(R.id.playButton)
        playButton.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply {
                action = MusicService.ACTION_PLAY
            }
            requireActivity().startService(intent)
        }

        // Handle pause button
        val pauseButton: Button = view.findViewById(R.id.pauseButton)
        pauseButton.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply {
                action = MusicService.ACTION_PAUSE
            }
            requireActivity().startService(intent)
        }

        // Handle stop button
        val stopButton: Button = view.findViewById(R.id.stopButton)
        stopButton.setOnClickListener {
            val intent = Intent(requireContext(), MusicService::class.java).apply {
                action = MusicService.ACTION_STOP
            }
            requireActivity().startService(intent)
        }

        return view
    }

    private fun applyDarkMode(isDarkMode: Boolean) {
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun applyLanguage(language: String) {
        val locale = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(locale)
    }
}
