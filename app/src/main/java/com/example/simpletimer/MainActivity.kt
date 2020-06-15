package com.example.simpletimer


import android.app.AlarmManager
import android.app.AlarmManager.AlarmClockInfo
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {


    private val REQUESTCODE = 100
    private var days = 4;
    private var hours = 0;
    private var minutes = 0;
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var cdTimer: CountDownTimer


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val daysValues = arrayOfNulls<String>(31)

        for (i in daysValues.indices) {
            daysValues[i] = "$i days"
        }

        numberPicker_days.minValue = 0
        numberPicker_days.maxValue = daysValues.size - 1
        numberPicker_days.value = 4
        numberPicker_days.wrapSelectorWheel = false
        numberPicker_days.displayedValues = daysValues

        // Set number picker value changed listener
        numberPicker_days.setOnValueChangedListener { picker, oldVal, newVal ->
            days = newVal
        }

        //////////////////////////////////////////////////////////////////////////////////////////////////
        //val hoursValues =  arrayOf("0 hours")

        val hoursValues = arrayOfNulls<String>(24)

        for (i in hoursValues.indices) {
            hoursValues[i] = "$i hours"
        }

        numberPicker_hours.minValue = 0
        numberPicker_hours.maxValue = hoursValues.size - 1
        numberPicker_hours.wrapSelectorWheel = false
        numberPicker_hours.displayedValues = hoursValues

        // Set number picker value changed listener
        numberPicker_hours.setOnValueChangedListener { picker, oldVal, newVal ->
            hours = newVal
        }

        //////////////////////////////////////////////////////////////
        val minutesValues = arrayOfNulls<String>(60)

        for (i in minutesValues.indices) {
            minutesValues[i] = "$i minutes"
        }

        numberPicker_minutes.minValue = 0
        numberPicker_minutes.maxValue = minutesValues.size - 1
        numberPicker_minutes.wrapSelectorWheel = false
        numberPicker_minutes.displayedValues = minutesValues

        // Set number picker value changed listener
        numberPicker_minutes.setOnValueChangedListener { picker, oldVal, newVal ->
            minutes = newVal
        }

        createNotificationChannel()
        // SQLite database handler
        //db = new SQLiteHandler(getApplicationContext());

        // Session manager
        var session = SharedPreferenceTime(applicationContext)
        // view_timer.format ="DD:HH:MM:SS"
        button_set.setOnClickListener {


            session.setTime(System.currentTimeMillis())
            var lengthMinutes = days*24*60 + hours*60 + minutes
            var lengthTimeLong = days*24*60*60*1000L + hours*60*60*1000L + minutes*60*1000L
            session.setLength(lengthTimeLong)
            updateTime();

            // Creating the pending intent to send to the BroadcastReceiver
            alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(this, BroadcastReciverNotification::class.java)
            pendingIntent = PendingIntent.getBroadcast(
                this,
                REQUESTCODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )

            // Setting the specific time for the alarm manager to trigger the intent,
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MINUTE, lengthMinutes)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setAlarmClock(
                    AlarmClockInfo(calendar.timeInMillis, pendingIntent),
                    pendingIntent
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
            } else {
                alarmManager[AlarmManager.RTC_WAKEUP, calendar.timeInMillis] = pendingIntent
            }

        }


    }

    override fun onStart() {
        super.onStart()
        updateTime()
    }

    private fun updateTime() {
        var session = SharedPreferenceTime(applicationContext)
        var startMS = session.getTime
        var lengthTime = session.getlength
        if (startMS != -1L) {

            var timeElapsed = System.currentTimeMillis() - startMS;
            Log.d(
                TAG,
                "timeElapsed ! : $timeElapsed"
            )

            var timeRemains = lengthTime - timeElapsed;

            Log.d(
                TAG,
                "timeRemains ! : $timeRemains"
            )




            if (this::cdTimer.isInitialized) {
                cdTimer.cancel()
            }
            cdTimer = object : CountDownTimer(timeRemains, 1000) {
                override fun onTick(millisUntilFinished: Long) {


                    val seconds = (millisUntilFinished / 1000) % 60
                    val minutes = (millisUntilFinished / (1000 * 60) % 60)
                    val hours = (millisUntilFinished / (1000 * 60 * 60) % 24)
                    val days = (millisUntilFinished / (1000 * 60 * 60 * 24))

                    textView.text =
                        "" + days + " ימים " + hours + "  שעות " + minutes + " דקות " + seconds + " שניות "
                }

                override fun onFinish() {
                    textView.text = "done!"
                }
            }.start()
        }
    }

    companion object {
        // LogCat tag
        private const val TAG = "timer_i"
        private const val CHANNEL_ID = "cd not";

    }


    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}