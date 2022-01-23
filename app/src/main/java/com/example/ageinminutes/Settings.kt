package com.example.ageinminutes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_settings.*

class Settings : AppCompatActivity() {

    var i = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setToolbar()

        toolbar_settings.setNavigationOnClickListener {
            onBackPressed()
        }

        toggleNightMode()

        tvDarkMode.setOnClickListener {
            i++
            if (i == 10) {
                intent = Intent(applicationContext, DateBirthRecorder::class.java)
                startActivity(intent)
            }
        }

    }

    private fun toggleNightMode() {
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)
        val sharedPrefsEditor: SharedPreferences.Editor = appSettingPrefs.edit()

        switchBtn.isChecked = isNightModeOn

        switchBtn.setOnCheckedChangeListener{ switchBtn, b ->
            if (isNightModeOn) {
                turnDarkModeOff(sharedPrefsEditor)
            } else {
                turnDarkModeOn(sharedPrefsEditor)
            }
        }
    }

    private fun turnDarkModeOff(sharedPrefsEditor : SharedPreferences.Editor){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sharedPrefsEditor.putBoolean("NightMode", false)
        sharedPrefsEditor.apply()
    }

    private fun turnDarkModeOn(sharedPrefsEditor : SharedPreferences.Editor) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        sharedPrefsEditor.putBoolean("NightMode", true)
        sharedPrefsEditor.apply()
    }

    private fun setToolbar() {
        setSupportActionBar(toolbar_settings)
        val actionbar = supportActionBar
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true)
        }
    }
}