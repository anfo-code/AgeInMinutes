package com.example.ageinminutes

import NotificationHandler
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import com.example.ageinminutes.database.DatabaseHandler
import com.example.ageinminutes.receiver.AlarmService
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var alarmService: AlarmService
    private var isAINShown: Boolean = false


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setNightMode()

        createActionBarDrawerToggle()


        navView.setNavigationItemSelectedListener {
            navigationListener(it)
            true
        }

        btnCalculate.setOnClickListener {

            if (!isAINShown) {
                editTextChecker(it)
            } else {
                editText.visibility = View.VISIBLE
                tvResult.visibility = View.GONE
                tvSelectedDate.visibility = View.GONE
                linearLayout.gravity = Gravity.CENTER
                isAINShown = false
                btnCalculate.text = "CALCULATE"
            }
        }



        createNotifications(this)

        val date = Date()
        val sdf = SimpleDateFormat("mmDDyyyy")
        Log.i("DATE:", sdf.format(date))
    }

    private fun createNotifications(context: Context){
        val notificationHandler = NotificationHandler(this)

        notificationHandler.setNotificationData()

    }

    private fun createActionBarDrawerToggle() {
        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun navigationListener(it: MenuItem) {
        when (it.itemId) {
            R.id.btn_settings -> {
                val intent = Intent(applicationContext, Settings::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setNightMode() {
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val isNightModeOn: Boolean = appSettingPrefs.getBoolean("NightMode", false)

        if (isNightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun editTextChecker(view: View) {
        if (editText.text.isNotEmpty()) {
            setBirthDayDate()
        } else {
            Snackbar.make(
                view,
                "Please, Enter Your Name!", Snackbar.LENGTH_SHORT
            ).show()
        }
    }

    private fun setBirthDayDate() {

        val myCalendar = Calendar.getInstance()
        val selectedYear = myCalendar.get(Calendar.YEAR)
        val selectedMonth = myCalendar.get(Calendar.MONTH)
        val selectedDayOfMonth = myCalendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(
            this, { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"

                addDateToDataBase(view, selectedDate)

                val differenceInMinutes = countAgeInMinutes(selectedDate)

                changeLayoutElements(selectedDate, differenceInMinutes)

            },
            selectedYear,
            selectedMonth,
            selectedDayOfMonth
        )


        dpd.datePicker.setMaxDate(Date().time - 86400000)
        dpd.show()

    }

    private fun countAgeInMinutes(selectedDate: String): Long {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)
        val birthDay = sdf.parse(selectedDate)
        val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))

        val selectedDateInMinutes = birthDay!!.time / 60000
        val currentDateToMinutes = currentDate!!.time / 60000
        return currentDateToMinutes - selectedDateInMinutes
    }

    private fun changeLayoutElements(selectedDate: String, differenceInMinutes: Long) {

        tvSelectedDate.text = "Your selected date is: \n $selectedDate"
        tvResult.text = "Is: \n" + differenceInMinutes.toString() + "\n minutes"
        editText.visibility = View.GONE
        tvResult.visibility = View.VISIBLE
        tvSelectedDate.visibility = View.VISIBLE
        btnCalculate.text = "TRY AGAIN"
        linearLayout.gravity = Gravity.CENTER_HORIZONTAL
        isAINShown = true
        editText.text.clear()
    }

    fun addDateToDataBase(view: View, birthday: String) {
        val name = editText.text.toString()
        val date = Date()
        val sdf = SimpleDateFormat("dd/MM/yyyy")
        val recordedDate = sdf.format(date)
        val databaseHandler = DatabaseHandler(this)
        val status = databaseHandler.addRecord(name, birthday, recordedDate)
    }

}