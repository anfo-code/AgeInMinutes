import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.ageinminutes.receiver.AlarmService
import com.example.ageinminutes.database.DatabaseHandler
import com.example.ageinminutes.database.RecordModel
import com.example.ageinminutes.receiver.AlarmReceiver
import java.util.*
import kotlin.collections.ArrayList

class NotificationHandler(private val context: Context) {

    public fun setNotificationData() {
        if (getItemsList().size > 0) {
            val items : ArrayList<RecordModel> = getItemsList()
            var position = 1
            val item = items.get(position)
            val birthDay = item.birthDate
            val name = item.name


            val alarmService = AlarmService(context)

            val nextBirthdayDate = countNextBirthdayDate(birthDay)

            val birthdayAlarmTime = getBirthdayAlarmTime(nextBirthdayDate)

            alarmService.setExactAlarm(birthdayAlarmTime, name)

        }
    }

    private fun getBirthdayAlarmTime(date: String) : Long{
        val sdf = SimpleDateFormat("dd/MM/yyyy hh:MM:ss")
        val time = date + " 8:00:00"
        Log.i("TIME:", time)
        val dateInMillis = sdf.parse(time).time
        return dateInMillis
    }

    fun getItemsList(): ArrayList<RecordModel> {
        val datebaseHandler = DatabaseHandler(context)

        val recList: ArrayList<RecordModel> = datebaseHandler.viewRecord()

        return recList
    }

    private fun countNextBirthdayDate(birthDay: String): String{

        val noYearDate = birthDay.substring(0, birthDay.length - 4)

        val sdf = SimpleDateFormat("yyyy")
        val currentYear = sdf.format(Date())

        val nextBirthdayDate = noYearDate + currentYear

        Log.i("DATE: ", nextBirthdayDate)

        return nextBirthdayDate
    }
}