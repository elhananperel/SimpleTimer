package com.example.simpletimer

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


class BroadcastReciverNotification : BroadcastReceiver() {


    companion object {
        private const val TAG = "BroadcastService"
        const val COUNTDOWN_BR = "com.eliperel.pumpsync.countdown_br"
        private const val CHANNEL_ID = "cd not";
    }

    override fun onReceive(context: Context?, intent: Intent?) {
//        val text = "Hello toast!"
//        val duration = Toast.LENGTH_LONG
//
//        val toast = Toast.makeText(context, text, duration)
//        toast.show()



        var builder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_local_pharmacy_black_24dp)
            .setContentTitle("gggggggggggggggg")
            .setContentText("gggggggggggge dgr ")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(12, builder.build())
        }
    }
}