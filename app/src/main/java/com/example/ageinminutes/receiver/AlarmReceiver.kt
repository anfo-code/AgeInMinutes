package com.example.ageinminutes.receiver

import NotificationHandler
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import androidx.core.app.NotificationCompat
import com.example.ageinminutes.util.Constants
import android.R
import android.app.PendingIntent
import android.provider.Settings.Global.getString
import androidx.core.app.NotificationManagerCompat
import com.example.ageinminutes.MainActivity


class AlarmReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        val notificationHandler = NotificationHandler(context)

        when (intent.action) {
            Constants.ACTION_SET_EXACT_ALARM -> {
                notificationHandler.setNotificationData()
            }


        }
    }

    private fun buildNotification(context: Context, name: String, date: String) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder = NotificationCompat.Builder(context, "NCidD1.0")
            .setSmallIcon(com.example.ageinminutes.R.drawable.ic_birthday)
            .setContentTitle("Don't Forget To Congratulate!")
            .setContentText("$name has birthday today! Hurry up, and congratulate them!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        builder.build()
    }

     fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()
}